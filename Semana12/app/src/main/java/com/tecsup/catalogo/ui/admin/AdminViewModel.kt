package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.data.repository.ReservaRepository
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AdminEstadisticas(
    val totalGuardado: Int = 0,
    val totalFavoritos: Int = 0,
    val totalReservas: Int = 0,
    val reservasPendientes: Int = 0,
    val reservasConfirmadas: Int = 0
)

class AdminViewModel(
    private val hotelRepository: HotelRepository,
    private val reservaRepository: ReservaRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estadisticas = MutableStateFlow(AdminEstadisticas())
    val estadisticas: StateFlow<AdminEstadisticas> = _estadisticas.asStateFlow()

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    val reservasRecientes: StateFlow<List<Reserva>> =
        reservaRepository.obtenerTodas()
            .map { list -> list.take(5) }
            .catch { emit(emptyList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun cargarEstadisticas() {
        viewModelScope.launch {
            val totalHoteles = hotelRepository.cantidadGuardada()
            val totalFavoritos = hotelRepository.cantidadFavoritos()
            val totalReservas = reservaRepository.contarTotal()
            val pendientes = reservaRepository.contarPorEstado("PENDIENTE")
            val confirmadas = reservaRepository.contarPorEstado("CONFIRMADA")

            _estadisticas.value = AdminEstadisticas(
                totalGuardado = totalHoteles,
                totalFavoritos = totalFavoritos,
                totalReservas = totalReservas,
                reservasPendientes = pendientes,
                reservasConfirmadas = confirmadas
            )
        }
    }

    fun cambiarEstadoReserva(reservaId: Long, nuevoEstado: String) {
        viewModelScope.launch {
            reservaRepository.actualizarEstado(reservaId, nuevoEstado)
            _mensaje.value = "Estado de reserva actualizado a $nuevoEstado"
            cargarEstadisticas()
        }
    }

    fun sincronizar() {
        if (_sincronizando.value) return
        viewModelScope.launch {
            _sincronizando.value = true
            hotelRepository.refrescar()
                .onSuccess { cantidad ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $cantidad elementos")
                    _mensaje.value = "Sincronizado: $cantidad elementos"
                }
                .onFailure { error ->
                    _mensaje.value = mensajeDe(error)
                }
            _sincronizando.value = false
            cargarEstadisticas()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            hotelRepository.limpiarCache()
            _mensaje.value = "Caché limpiada"
            cargarEstadisticas()
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}
