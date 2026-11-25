package com.lino.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lino.murocampus.data.PostRepository
import com.lino.murocampus.model.Comentario
import com.lino.murocampus.model.NuevoPost
import com.lino.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _envio = MutableStateFlow<UiState<Post>?>(null)
    val envio: StateFlow<UiState<Post>?> = _envio.asStateFlow()

    private val _borrado = MutableStateFlow<UiState<Unit>?>(null)
    val borrado: StateFlow<UiState<Unit>?> = _borrado.asStateFlow()

    private var listaJob: Job? = null
    private var detalleJob: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        listaJob?.cancel()
        _lista.value = UiState.Cargando
        val filtro = _usuarioFiltro.value
        listaJob = viewModelScope.launch {
            try {
                val resultado = if (filtro == null) repo.listar() else repo.porUsuario(filtro)
                resultado
                    .onSuccess { _lista.value = UiState.Exito(it) }
                    .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {
        detalleJob?.cancel()
        _detalle.value = UiState.Cargando
        detalleJob = viewModelScope.launch {
            try {
                val datos = coroutineScope {
                    val postDef = async { repo.detalle(id) }
                    val comentariosDef = async { repo.comentarios(id) }
                    DetalleUi(
                        post = postDef.await().getOrThrow(),
                        comentarios = comentariosDef.await().getOrThrow()
                    )
                }
                _detalle.value = UiState.Exito(datos)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _detalle.value = UiState.Error(mensajeDe(e))
            }
        }
    }

    fun publicar(titulo: String, texto: String) {
        if (_envio.value is UiState.Cargando) return
        _envio.value = UiState.Cargando
        viewModelScope.launch {
            try {
                repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                    .onSuccess { _envio.value = UiState.Exito(it) }
                    .onFailure { _envio.value = UiState.Error(mensajeDe(it)) }
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun limpiarEnvio() {
        _envio.value = null
    }

    fun borrar(id: Int) {
        if (_borrado.value is UiState.Cargando || _borrado.value is UiState.Exito) return
        _borrado.value = UiState.Cargando
        viewModelScope.launch {
            try {
                repo.borrar(id)
                    .onSuccess {
                        _borrado.value = UiState.Exito(Unit)
                        val actual = _lista.value
                        if (actual is UiState.Exito) {
                            _lista.value = UiState.Exito(actual.datos.filterNot { it.id == id })
                        }
                    }
                    .onFailure { _borrado.value = UiState.Error(mensajeDe(it)) }
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun limpiarBorrado() {
        _borrado.value = null
    }
}
