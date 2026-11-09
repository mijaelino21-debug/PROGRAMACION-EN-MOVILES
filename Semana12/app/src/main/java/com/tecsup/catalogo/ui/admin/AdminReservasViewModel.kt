package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.ReservaRepository
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AdminReservasEstadisticas(
    val total: Int = 0,
    val pendientes: Int = 0,
    val confirmadas: Int = 0,
    val rechazadas: Int = 0
)

enum class OrdenReserva { FECHA_SOLICITUD, FECHA_ENTRADA }

class AdminReservasViewModel(
    private val reservaRepository: ReservaRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Todas, 1: Pendientes, 2: Confirmadas, 3: Rechazadas
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _sortOrder = MutableStateFlow(OrdenReserva.FECHA_SOLICITUD)
    val sortOrder: StateFlow<OrdenReserva> = _sortOrder.asStateFlow()

    private val _estadisticas = MutableStateFlow(AdminReservasEstadisticas())
    val estadisticas: StateFlow<AdminReservasEstadisticas> = _estadisticas.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargarEstadisticas()
    }

    val estado: StateFlow<UiState<List<Reserva>>> =
        combine(
            reservaRepository.obtenerTodas(),
            _searchQuery,
            _selectedTab,
            _sortOrder
        ) { list, queryRaw, tab, sort ->
            val query = queryRaw.trim().lowercase()

            // Filtrar por pestañas
            val filtradasTab = when (tab) {
                1 -> list.filter { it.estado == "PENDIENTE" }
                2 -> list.filter { it.estado == "CONFIRMADA" }
                3 -> list.filter { it.estado == "RECHAZADA" || it.estado == "CANCELADA" }
                else -> list
            }

            // Filtrar por buscador (hotel, ciudad o id de reserva)
            val filtradasBusqueda = if (query.isEmpty()) {
                filtradasTab
            } else {
                val idQuery = query.removePrefix("#")
                filtradasTab.filter {
                    it.nombreHotel.lowercase().contains(query) ||
                            it.ciudad.lowercase().contains(query) ||
                            it.id.toString().contains(idQuery)
                }
            }

            // Ordenar
            val ordenadas = when (sort) {
                OrdenReserva.FECHA_SOLICITUD -> filtradasBusqueda.sortedByDescending { it.fechaCreacion }
                OrdenReserva.FECHA_ENTRADA -> filtradasBusqueda.sortedBy { it.fechaEntrada }
            }

            UiState.Exito(ordenadas) as UiState<List<Reserva>>
        }
            .catch { emit(UiState.Error(it.localizedMessage ?: "Error al cargar reservas")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Cargando
            )

    fun cargarEstadisticas() {
        viewModelScope.launch {
            val total = reservaRepository.contarTotal()
            val pendientes = reservaRepository.contarPorEstado("PENDIENTE")
            val confirmadas = reservaRepository.contarPorEstado("CONFIRMADA")
            val rechazadas = reservaRepository.contarPorEstado("RECHAZADA") + reservaRepository.contarPorEstado("CANCELADA")

            _estadisticas.value = AdminReservasEstadisticas(
                total = total,
                pendientes = pendientes,
                confirmadas = confirmadas,
                rechazadas = rechazadas
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun onSortOrderChanged(order: OrdenReserva) {
        _sortOrder.value = order
    }

    fun confirmarReserva(reservaId: Long) {
        viewModelScope.launch {
            reservaRepository.actualizarEstado(reservaId, "CONFIRMADA")
            _mensaje.value = "Reserva #$reservaId confirmada exitosamente"
            cargarEstadisticas()
        }
    }

    fun rechazarReserva(reservaId: Long, motivo: String) {
        viewModelScope.launch {
            reservaRepository.actualizarEstado(reservaId, "RECHAZADA", motivo)
            _mensaje.value = "Reserva #$reservaId rechazada"
            cargarEstadisticas()
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}