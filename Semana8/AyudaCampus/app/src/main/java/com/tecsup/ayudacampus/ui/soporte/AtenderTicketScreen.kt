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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.BurbujaComentario
import com.tecsup.ayudacampus.ui.theme.color
import com.tecsup.ayudacampus.viewmodel.AtenderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AtenderTicketScreen(vm: AtenderViewModel, ticketId: Int, onVolver: () -> Unit) {
    LaunchedEffect(ticketId) { vm.cargar(ticketId) }
    LaunchedEffect(vm.guardado) { if (vm.guardado) onVolver() }

    val detalle by vm.ticket.collectAsState()
    var respuesta by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Atender ticket #%04d".format(ticketId), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        val d = detalle ?: return@Column
        val t = d.ticket
        val cerrado = t.estado == EstadoTicket.CERRADO.name
        val estadoActual = vm.estadoSel ?: EstadoTicket.valueOf(t.estado)
        val prioridadActual = vm.prioridadSel ?: Prioridad.valueOf(t.prioridad)

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Datos del ticket y del cliente
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(t.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(
                            text = vm.calcularSla(t),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (vm.calcularSla(t).contains("Vencido")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        "${d.cliente.nombre} · ${t.ubicacion} · ${d.categoria.nombre}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(t.descripcion)
                }
            }

            Text("Estado", fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(EstadoTicket.entries) { e ->
                    FilterChip(
                        selected = estadoActual == e,
                        onClick = { vm.cambiarEstado(e) },
                        label = { Text(e.etiqueta) },
                        // CERRADO siempre deshabilitado para Soporte
                        enabled = !cerrado && e != EstadoTicket.CERRADO,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Text("Prioridad", fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Prioridad.entries.forEach { p ->
                    BotonPrioridad(p, prioridadActual == p, !cerrado, Modifier.weight(1f)) { vm.cambiarPrioridad(p) }
                }
            }

            Text("Conversación", fontWeight = FontWeight.Bold)
            // Mensajes propios (Soporte) a la derecha
            d.comentarios.sortedBy { it.fecha }.forEach { c ->
                BurbujaComentario(
                    autor = c.autorNombre,
                    texto = c.texto,
                    hora = hora(c.fecha),
                    propio = c.autorRol == Rol.ADMIN.name,
                    esInterno = c.esInterno
                )
            }
            Spacer(Modifier.height(4.dp))
        }

        if (!cerrado) {
            var esNotaInterna by remember { mutableStateOf(false) }

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = esNotaInterna,
                    onClick = { esNotaInterna = !esNotaInterna },
                    label = { Text(if (esNotaInterna) "🔒 Nota Interna (Privada)" else "💬 Respuesta Pública") }
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = respuesta,
                    onValueChange = { respuesta = it },
                    placeholder = { Text(if (esNotaInterna) "Escribe nota interna..." else "Responder al cliente...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
                IconButton(onClick = {
                    if (esNotaInterna) {
                        vm.comentarInterno(respuesta)
                    } else {
                        vm.responder(respuesta)
                    }
                    respuesta = ""
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = if (esNotaInterna) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary)
                }
            }
            AyudaButton(
                texto = "Guardar cambios",
                onClick = { vm.guardar() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun BotonPrioridad(
    p: Prioridad,
    seleccionado: Boolean,
    habilitado: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    if (seleccionado) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = habilitado,
            colors = ButtonDefaults.buttonColors(containerColor = p.color())
        ) { Text(p.etiqueta) }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier, enabled = habilitado) { Text(p.etiqueta) }
    }
}

private fun hora(fecha: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(fecha))