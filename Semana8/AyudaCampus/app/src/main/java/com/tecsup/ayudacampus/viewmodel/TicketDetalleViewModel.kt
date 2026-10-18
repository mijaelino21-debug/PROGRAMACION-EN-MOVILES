package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TicketDetalleViewModel(private val repo: AyudaRepository) : ViewModel() {

    private val ticketId = MutableStateFlow(0)

    // Flow: si Soporte cambia algo, la pantalla se actualiza sola
    val ticket: StateFlow<TicketConDetalle?> = ticketId
        .flatMapLatest { repo.observarTicket(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun cargar(id: Int) { ticketId.value = id }

    fun comentar(texto: String) {
        val t = ticket.value ?: return
        // No se comenta vacio ni en un ticket cerrado
        if (texto.isBlank() || t.ticket.estado == EstadoTicket.CERRADO.name) return
        viewModelScope.launch { repo.agregarComentario(t.ticket.id, t.cliente, texto.trim()) }
    }

    fun cerrar(calificacion: Int, comentarioFinal: String = "") {
        val t = ticket.value ?: return
        // Solo si esta RESUELTO y con calificacion de 1 a 5
        if (t.ticket.estado != EstadoTicket.RESUELTO.name || calificacion !in 1..5) return
        viewModelScope.launch {
            if (comentarioFinal.isNotBlank()) {
                repo.agregarComentario(t.ticket.id, t.cliente, comentarioFinal.trim())
            }
            repo.cerrarConCalificacion(t.ticket.id, calificacion)
        }
    }

    fun reabrir() {
        val t = ticket.value ?: return
        // Solo si esta RESUELTO
        if (t.ticket.estado != EstadoTicket.RESUELTO.name) return
        viewModelScope.launch { repo.reabrir(t.ticket.id) }
    }

    fun calcularSla(ticket: Ticket): String = SlaUtils.calcularSla(ticket)
}