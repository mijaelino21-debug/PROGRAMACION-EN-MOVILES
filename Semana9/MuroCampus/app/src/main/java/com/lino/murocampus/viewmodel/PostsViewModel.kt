package com.lino.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lino.murocampus.data.PostRepository
import com.lino.murocampus.model.Comentario
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

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var detalleJob: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
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
}
