package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.TicketCard
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import androidx.compose.material3.FilterChipDefaults

@Composable
fun MisTicketsScreen(
    vm: MisTicketsViewModel,
    onAbrirTicket: (Int) -> Unit,
    onNuevo: () -> Unit
) {
    val tickets by vm.tickets.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val abiertos by vm.abiertos.collectAsState()
    val nombre by vm.nombre.collectAsState()

    // "Todos" incluye tambien los CERRADOS
    val filtros = listOf(
        null to "Todos",
        EstadoTicket.ABIERTO to "Abiertos",
        EstadoTicket.EN_PROCESO to "En proceso",
        EstadoTicket.RESUELTO to "Resueltos"
    )

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Mis tickets", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Hola, $nombre · $abiertos abiertos", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))

            AyudaTextField(
                valor = busqueda,
                onCambio = { vm.cambiarBusqueda(it) },
                etiqueta = "Buscar por título o número",
                icono = Icons.Default.Search
            )
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
                    Text("No tienes tickets aquí", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(tickets, key = { it.ticket.id }) { t ->
                        TicketCard(
                            titulo = t.ticket.titulo,
                            detalle = "#%04d · %s".format(t.ticket.id, t.ticket.ubicacion),
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

        FloatingActionButton(
            onClick = onNuevo,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo ticket")
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