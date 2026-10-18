package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BandejaViewModel(repo: AyudaRepository) : ViewModel() {

    private val _filtro = MutableStateFlow<EstadoTicket?>(null)   // null = Todos
    val filtro: StateFlow<EstadoTicket?> = _filtro

    // Filtra por estado sin perder el orden por prioridad que entrega el DAO
    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(repo.observarTodosLosTickets(), _filtro) { lista, estado ->
            if (estado == null) lista else lista.filter { it.ticket.estado == estado.name }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarFiltro(estado: EstadoTicket?) { _filtro.value = estado }
}