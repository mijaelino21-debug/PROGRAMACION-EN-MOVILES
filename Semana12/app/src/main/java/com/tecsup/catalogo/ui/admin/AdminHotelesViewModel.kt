package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminHotelesViewModel(
    private val hotelRepository: HotelRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Hotel>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Hotel>>> = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private var todosLosHoteles: List<Hotel> = emptyList()
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            hotelRepository.items.collect { lista ->
                todosLosHoteles = lista
                if (lista.isNotEmpty() || refrescoTerminado) {
                    _estado.value = UiState.Exito(lista)
                }
            }
        }
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            val resultado = hotelRepository.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess {
                    if (todosLosHoteles.isEmpty()) {
                        _estado.value = UiState.Exito(emptyList())
                    }
                }
                .onFailure { error ->
                    val totalGuardado = hotelRepository.cantidadGuardada()
                    if (totalGuardado == 0) {
                        _estado.value = UiState.Error(mensajeDe(error))
                    } else {
                        _mensaje.value = "Error al actualizar datos: ${mensajeDe(error)}"
                        _estado.value = UiState.Exito(todosLosHoteles)
                    }
                }
        }
    }

    fun reintentar() {
        _estado.value = UiState.Cargando
        refrescoTerminado = false
        refrescar()
    }

    fun guardarHotel(hotel: Hotel) {
        viewModelScope.launch {
            if (hotel.id > 0) {
                hotelRepository.actualizarHotel(hotel)
                    .onSuccess {
                        _mensaje.value = "Hotel actualizado exitosamente"
                        refrescar()
                    }
                    .onFailure { _mensaje.value = "Error al actualizar: ${it.message}" }
            } else {
                hotelRepository.agregarHotel(hotel)
                    .onSuccess {
                        _mensaje.value = "Hotel agregado exitosamente"
                        refrescar()
                    }
                    .onFailure { _mensaje.value = "Error al agregar: ${it.message}" }
            }
        }
    }

    fun eliminarHotel(id: Int) {
        viewModelScope.launch {
            hotelRepository.eliminarHotel(id)
                .onSuccess {
                    _mensaje.value = "Hotel eliminado"
                    refrescar()
                }
                .onFailure { _mensaje.value = "Error al eliminar: ${it.message}" }
        }
    }

    fun cambiarEstado(id: Int, activo: Boolean) {
        viewModelScope.launch {
            hotelRepository.cambiarEstadoHotel(id, activo)
                .onSuccess {
                    _mensaje.value = if (activo) "Hotel activado" else "Hotel desactivado"
                    refrescar()
                }
                .onFailure { error ->
                    _mensaje.value = "Error al cambiar estado: ${mensajeDe(error)}"
                }
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}

