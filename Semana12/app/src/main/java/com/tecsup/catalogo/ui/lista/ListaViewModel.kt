package com.tecsup.catalogo.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repository: HotelRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Hotel>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Hotel>>> = _estado.asStateFlow()

    // Aviso de "sin conexión" (null = todo bien)
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _soloFavoritos = MutableStateFlow(false)
    val soloFavoritos: StateFlow<Boolean> = _soloFavoritos.asStateFlow()

    private var todosLosHoteles: List<Hotel> = emptyList()
    private var refrescoTerminado = false

    init {
        // La lista SIEMPRE sale de Room. Retrofit solo la actualiza.
        viewModelScope.launch {
            repository.items.collect { lista ->
                todosLosHoteles = lista
                // No mostramos "vacío" antes de que termine el primer refresco
                if (lista.isNotEmpty() || refrescoTerminado) publicar()
            }
        }
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            val resultado = repository.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { cantidad ->
                    _aviso.value = null
                    if (cantidad == 0) publicar()
                    notificador.mostrar("Datos actualizados", "Se guardaron $cantidad elementos")
                }
                .onFailure { error ->
                    val totalGuardado = repository.cantidadGuardada()
                    if (totalGuardado == 0) {
                        _estado.value = UiState.Error(mensajeDe(error))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                        publicar()
                    }
                }
        }
    }

    fun reintentar() {
        _estado.value = UiState.Cargando
        refrescoTerminado = false
        refrescar()
    }

    private fun publicar() {
        _estado.value = UiState.Exito(
            if (_soloFavoritos.value) todosLosHoteles.filter { it.esFavorito } else todosLosHoteles
        )
    }

    fun toggleFiltroFavoritos() {
        _soloFavoritos.value = !_soloFavoritos.value
        if (_estado.value is UiState.Exito) publicar()
    }

    fun onFavoritoClicked(hotelId: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(hotelId)
        }
    }
}