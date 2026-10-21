package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.tecsup.ayudacampus.ui.components.EmptyState
import com.tecsup.ayudacampus.ui.components.TicketCard
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel

@Composable
fun BandejaScreen(vm: BandejaViewModel, onAbrirTicket: (Int) -> Unit) {
    val tickets by vm.tickets.collectAsState()
    val filtroEstado by vm.filtro.collectAsState()
    val filtroPrioridad by vm.filtroPrioridad.collectAsState()
    val filtroCategoria by vm.filtroCategoria.collectAsState()
    val filtroTexto by vm.filtroTexto.collectAsState()
    val categorias by vm.categorias.collectAsState()

    val filtrosEstado = listOf(
        null to "Todos los estados",
        EstadoTicket.ABIERTO to "Abiertos",
        EstadoTicket.EN_PROCESO to "En proceso",
        EstadoTicket.RESUELTO to "Resueltos"
    )

    val hayFiltrosActivos = filtroEstado != null || filtroPrioridad != null || filtroCategoria != null || filtroTexto.isNotBlank()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tickets", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            if (hayFiltrosActivos) {
                TextButton(onClick = { vm.limpiarFiltros() }) {
                    Text("Limpiar filtros", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        AyudaTextField(
            valor = filtroTexto,
            onCambio = { vm.cambiarFiltroTexto(it) },
            etiqueta = "Buscar por título, cliente, ID...",
            icono = Icons.Default.Search
        )

        Spacer(Modifier.height(8.dp))

        // Filtro de Estado
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtrosEstado) { (estado, texto) ->
                FilterChip(
                    selected = filtroEstado == estado,
                    onClick = { vm.cambiarFiltro(estado) },
                    label = { Text(texto) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // Filtro de Prioridad
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = filtroPrioridad == null,
                    onClick = { vm.cambiarFiltroPrioridad(null) },
                    label = { Text("Todas prioridades") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
            items(Prioridad.entries.toTypedArray()) { prioridad ->
                FilterChip(
                    selected = filtroPrioridad == prioridad,
                    onClick = { vm.cambiarFiltroPrioridad(prioridad) },
                    label = { Text("Prioridad ${prioridad.etiqueta}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // Filtro de Categoría
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = filtroCategoria == null,
                    onClick = { vm.cambiarFiltroCategoria(null) },
                    label = { Text("Todas categorías") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
            items(categorias, key = { it.id }) { cat ->
                FilterChip(
                    selected = filtroCategoria == cat.id,
                    onClick = { vm.cambiarFiltroCategoria(cat.id) },
                    label = { Text(cat.nombre) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (tickets.isEmpty()) {
            EmptyState(
                icono = if (hayFiltrosActivos) Icons.Default.Search else Icons.Default.Inbox,
                titulo = if (hayFiltrosActivos) "No hay tickets con los filtros seleccionados" else "No hay tickets aquí",
                subtitulo = if (hayFiltrosActivos) "Prueba cambiando o limpiando los filtros de búsqueda." else "La bandeja de soporte está al día."
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tickets, key = { it.ticket.id }) { t ->
                    TicketCard(
                        titulo = t.ticket.titulo,
                        detalle = "#%04d · %s".format(t.ticket.id, t.cliente.nombre),
                        icono = t.categoria.icono,
                        estado = EstadoTicket.valueOf(t.ticket.estado),
                        prioridad = Prioridad.valueOf(t.ticket.prioridad),
                        tiempo = vm.calcularHace(t.ticket.fecha),
                        sla = vm.calcularSla(t.ticket),
                        onClick = { onAbrirTicket(t.ticket.id) }
                    )
                }
            }
        }
    }
}
