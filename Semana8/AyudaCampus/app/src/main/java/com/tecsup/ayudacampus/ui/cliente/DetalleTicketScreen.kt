package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.BurbujaComentario
import com.tecsup.ayudacampus.ui.components.EstadoChip
import com.tecsup.ayudacampus.ui.components.Estrellas
import com.tecsup.ayudacampus.ui.components.PrioridadChip
import com.tecsup.ayudacampus.ui.components.StepperTicket
import com.tecsup.ayudacampus.ui.theme.ColorResuelto
import com.tecsup.ayudacampus.viewmodel.TicketDetalleViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetalleTicketScreen(vm: TicketDetalleViewModel, ticketId: Int, onVolver: () -> Unit) {
    LaunchedEffect(ticketId) { vm.cargar(ticketId) }

    val detalle by vm.ticket.collectAsState()
    var texto by remember { mutableStateOf("") }
    var calificacion by remember { mutableIntStateOf(0) }
    var comentarioFinal by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Ticket #%04d".format(ticketId), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        val d = detalle ?: return@Column
        val t = d.ticket
        val estado = EstadoTicket.valueOf(t.estado)

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cabecera
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(t.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        EstadoChip(estado)
                        PrioridadChip(Prioridad.valueOf(t.prioridad))
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${d.categoria.nombre} · ${t.ubicacion}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = vm.calcularSla(t),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (vm.calcularSla(t).contains("Vencido")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            StepperTicket(estado)

            if (estado == EstadoTicket.RESUELTO) {
                Surface(
                    color = ColorResuelto.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Soporte marcó este ticket como resuelto. ¿El problema se solucionó?",
                        color = ColorResuelto,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Text("Conversación", fontWeight = FontWeight.Bold)
            // Mensajes propios (Estudiante) a la derecha
            d.comentarios.sortedBy { it.fecha }.forEach { c ->
                BurbujaComentario(
                    autor = c.autorNombre,
                    texto = c.texto,
                    hora = hora(c.fecha),
                    propio = c.autorRol == Rol.CLIENTE.name,
                    esInterno = c.esInterno
                )
            }

            if (estado == EstadoTicket.RESUELTO) {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Califica la atención", fontWeight = FontWeight.Bold)
                        Estrellas(valor = calificacion, onCambio = { calificacion = it })
                        Text("$calificacion de 5 estrellas", style = MaterialTheme.typography.bodySmall)
                    }
                }
                AyudaTextField(
                    valor = comentarioFinal,
                    onCambio = { comentarioFinal = it },
                    etiqueta = "Comentario final (opcional)",
                    unaLinea = false
                )
                // Deshabilitado mientras la calificacion sea 0
                AyudaButton(
                    texto = "Confirmar y cerrar",
                    onClick = { vm.cerrar(calificacion, comentarioFinal) },
                    enabled = calificacion > 0
                )
                OutlinedButton(
                    onClick = { vm.reabrir() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Reabrir ticket", fontWeight = FontWeight.Bold) }
            }

            if (estado == EstadoTicket.CERRADO) {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Calificación recibida", fontWeight = FontWeight.Bold)
                        Estrellas(valor = t.calificacion ?: 0)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Campo para comentar (no aparece si esta cerrado)
        if (estado == EstadoTicket.ABIERTO || estado == EstadoTicket.EN_PROCESO) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    placeholder = { Text("Escribe un comentario...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
                IconButton(onClick = {
                    vm.comentar(texto)
                    texto = ""
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

private fun hora(fecha: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(fecha))