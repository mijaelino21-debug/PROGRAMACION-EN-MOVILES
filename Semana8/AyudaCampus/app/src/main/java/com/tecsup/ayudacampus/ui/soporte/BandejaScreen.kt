package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.components.TicketCard
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel

@Composable
fun BandejaScreen(vm: BandejaViewModel, onAbrirTicket: (Int) -> Unit) {
    val tickets by vm.tickets.collectAsState()
    val filtro by vm.filtro.collectAsState()

    val filtros = listOf(
        null to "Todos",
        EstadoTicket.ABIERTO to "Abiertos",
        EstadoTicket.EN_PROCESO to "En proceso",
        EstadoTicket.RESUELTO to "Resueltos"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Tickets", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtros) { (estado, texto) ->
                FilterChip(
                    selected = filtro == estado,
                    onClick = { vm.cambiarFiltro(estado) },
                    label = { Text(texto) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        if (tickets.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay tickets aquí", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tickets, key = { it.ticket.id }) { t ->
                    TicketCard(
                        titulo = t.ticket.titulo,
                        detalle = "#%04d · %s".format(t.ticket.id, t.cliente.nombre),
                        icono = t.categoria.icono,
                        estado = EstadoTicket.valueOf(t.ticket.estado),
                        prioridad = Prioridad.valueOf(t.ticket.prioridad),
                        tiempo = hace(t.ticket.fecha),
                        onClick = { onAbrirTicket(t.ticket.id) }
                    )
                }
            }
        }
    }
}

private fun hace(fecha: Long): String {
    val min = (System.currentTimeMillis() - fecha) / 60_000
    return when {
        min < 60 -> "Hace $min min"
        min < 60 * 24 -> "Hace ${min / 60} h"
        else -> "Hace ${min / (60 * 24)} d"
    }
}