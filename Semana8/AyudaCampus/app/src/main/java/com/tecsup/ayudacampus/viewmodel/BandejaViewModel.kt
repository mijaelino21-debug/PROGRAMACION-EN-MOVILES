package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BandejaViewModel(repo: AyudaRepository) : ViewModel() {

    private val _filtro = MutableStateFlow<EstadoTicket?>(null)   // null = Todos
    val filtro: StateFlow<EstadoTicket?> = _filtro

    private val _filtroPrioridad = MutableStateFlow<Prioridad?>(null) // null = Todas
    val filtroPrioridad: StateFlow<Prioridad?> = _filtroPrioridad

    private val _filtroCategoria = MutableStateFlow<Int?>(null)  // null = Todas
    val filtroCategoria: StateFlow<Int?> = _filtroCategoria

    private val _filtroTexto = MutableStateFlow("")
    val filtroTexto: StateFlow<String> = _filtroTexto

    val categorias: StateFlow<List<Categoria>> =
        repo.observarCategorias()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtra por estado, prioridad, categoría y texto al mismo tiempo
    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(
            repo.observarTodosLosTickets(),
            _filtro,
            _filtroPrioridad,
            _filtroCategoria,
            _filtroTexto
        ) { lista, estado, prioridad, categoriaId, texto ->
            val q = texto.trim().removePrefix("#")
            lista.filter { t ->
                val cumpleEstado = estado == null || t.ticket.estado == estado.name
                val cumplePrioridad = prioridad == null || t.ticket.prioridad == prioridad.name
                val cumpleCategoria = categoriaId == null || t.ticket.categoriaId == categoriaId
                val cumpleTexto = q.isEmpty() ||
                        t.ticket.titulo.contains(q, ignoreCase = true) ||
                        t.ticket.descripcion.contains(q, ignoreCase = true) ||
                        t.cliente.nombre.contains(q, ignoreCase = true) ||
                        t.ticket.ubicacion.contains(q, ignoreCase = true) ||
                        t.ticket.id.toString().padStart(4, '0').contains(q)

                cumpleEstado && cumplePrioridad && cumpleCategoria && cumpleTexto
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarFiltro(estado: EstadoTicket?) { _filtro.value = estado }
    fun cambiarFiltroPrioridad(prioridad: Prioridad?) { _filtroPrioridad.value = prioridad }
    fun cambiarFiltroCategoria(categoriaId: Int?) { _filtroCategoria.value = categoriaId }
    fun cambiarFiltroTexto(texto: String) { _filtroTexto.value = texto }

    fun limpiarFiltros() {
        _filtro.value = null
        _filtroPrioridad.value = null
        _filtroCategoria.value = null
        _filtroTexto.value = ""
    }

    fun calcularSla(ticket: Ticket): String = SlaUtils.calcularSla(ticket)
    fun calcularHace(fecha: Long): String = SlaUtils.calcularHace(fecha)
}