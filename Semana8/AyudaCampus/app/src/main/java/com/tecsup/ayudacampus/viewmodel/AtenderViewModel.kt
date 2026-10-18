package com.tecsup.ayudacampus.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.model.Rol
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AtenderViewModel(
    private val repo: AyudaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val ticketId = MutableStateFlow(0)

    val ticket: StateFlow<TicketConDetalle?> = ticketId
        .flatMapLatest { repo.observarTicket(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Cambios elegidos que se aplican al presionar "Guardar cambios"
    var estadoSel by mutableStateOf<EstadoTicket?>(null); private set
    var prioridadSel by mutableStateOf<Prioridad?>(null); private set
    var guardado by mutableStateOf(false); private set

    private fun estaCerrado() = ticket.value?.ticket?.estado == EstadoTicket.CERRADO.name

    fun cargar(id: Int) { ticketId.value = id }

    fun cambiarEstado(estado: EstadoTicket) {
        // Soporte no puede cerrar, ni cambiar un ticket ya cerrado
        if (estado == EstadoTicket.CERRADO || estaCerrado()) return
        estadoSel = estado
    }

    fun cambiarPrioridad(p: Prioridad) {
        if (estaCerrado()) return
        prioridadSel = p
    }

    fun responder(texto: String) {
        val t = ticket.value ?: return
        if (texto.isBlank() || estaCerrado()) return
        viewModelScope.launch {
            val autor = Usuario(
                id = prefs.usuarioId.first(),
                nombre = "Soporte TI",
                correo = "",
                clave = "",
                rol = Rol.ADMIN.name
            )
            repo.agregarComentario(t.ticket.id, autor, texto.trim())
        }
    }

    fun guardar() {
        val t = ticket.value ?: return
        if (estaCerrado()) return
        viewModelScope.launch {
            estadoSel?.let { repo.cambiarEstado(t.ticket.id, it) }
            prioridadSel?.let { repo.cambiarPrioridad(t.ticket.id, it) }
            guardado = true
        }
    }
}