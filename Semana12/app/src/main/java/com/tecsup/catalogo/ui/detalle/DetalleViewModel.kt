package com.tecsup.catalogo.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.auth.AuthRepository
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.data.repository.ReservaRepository
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repository: HotelRepository,
    private val reservaRepository: ReservaRepository,
    private val authRepository: AuthRepository,
    private val hotelId: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Hotel>>(UiState.Cargando)
    val estado: StateFlow<UiState<Hotel>> = _estado.asStateFlow()

    private val _reservaEstado = MutableStateFlow<UiState<Unit>?>(null)
    val reservaEstado: StateFlow<UiState<Unit>?> = _reservaEstado.asStateFlow()

    init { cargar() }

    fun cargar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repository.obtenerDetalle(hotelId)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun alternarFavorito() {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            repository.alternarFavorito(actual.id)
            _estado.value = UiState.Exito(actual.copy(esFavorito = !actual.esFavorito))
        }
    }

    fun crearReserva(
        fechaEntrada: String,
        fechaSalida: String,
        cantidadHuespedes: Int,
        cantidadNoches: Int,
        precioTotal: Double
    ) {
        val hotel = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            val sesion = authRepository.sesionActual()
            if (sesion?.rol == Rol.ADMIN) {
                _reservaEstado.value = UiState.Error("Los administradores no pueden realizar reservas.")
                return@launch
            }
            if (!authRepository.hayUsuario) {
                _reservaEstado.value = UiState.Error("Usuario no autenticado")
                return@launch
            }
            _reservaEstado.value = UiState.Cargando
            val uid = sesion?.uid ?: authRepository.usuarioUid.value ?: ""
            val reserva = Reserva(
                usuarioId = uid,
                hotelId = hotel.id,
                nombreHotel = hotel.nombre,
                imagenUrl = hotel.imagenUrl,
                ciudad = hotel.ciudad,
                fechaEntrada = fechaEntrada.trim(),
                fechaSalida = fechaSalida.trim(),
                cantidadHuespedes = cantidadHuespedes,
                precioPorNoche = hotel.precioNoche,
                cantidadNoches = cantidadNoches,
                precioTotal = precioTotal
            )
            reservaRepository.guardarReserva(reserva)
                .onSuccess { _reservaEstado.value = UiState.Exito(Unit) }
                .onFailure { _reservaEstado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarReservaEstado() {
        _reservaEstado.value = null
    }
}
