package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class MisTicketsViewModel(
    repo: AyudaRepository,
    prefs: PreferenciasStore
) : ViewModel() {

    // Tickets del usuario actual (su id viene de DataStore)
    private val misTickets: Flow<List<TicketConDetalle>> =
        prefs.usuarioId.flatMapLatest { repo.observarTicketsCliente(it) }

    private val _filtro = MutableStateFlow<EstadoTicket?>(null)   // null = Todos
    val filtro: StateFlow<EstadoTicket?> = _filtro

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    // Combina los 3 flujos: lista, estado elegido y texto de busqueda
    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(misTickets, _filtro, _busqueda) { lista, estado, texto ->
            val q = texto.trim().removePrefix("#")
            lista.filter { t ->
                (estado == null || t.ticket.estado == estado.name) &&
                        (q.isEmpty() ||
                                t.ticket.titulo.contains(q, ignoreCase = true) ||
                                t.ticket.id.toString().padStart(4, '0').contains(q))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Contadores (Mis tickets y Perfil)
    val abiertos: StateFlow<Int> = misTickets
        .map { lista -> lista.count { it.ticket.estado == EstadoTicket.ABIERTO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val resueltos: StateFlow<Int> = misTickets
        .map { lista -> lista.count { it.ticket.estado == EstadoTicket.RESUELTO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val total: StateFlow<Int> = misTickets
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val nombre: StateFlow<String> = misTickets
        .map { it.firstOrNull()?.cliente?.nombre?.substringBefore(" ") ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun cambiarFiltro(estado: EstadoTicket?) { _filtro.value = estado }
    fun cambiarBusqueda(texto: String) { _busqueda.value = texto }
}