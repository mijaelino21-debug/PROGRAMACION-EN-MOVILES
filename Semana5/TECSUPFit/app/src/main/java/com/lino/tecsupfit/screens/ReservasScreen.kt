package com.lino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lino.tecsupfit.model.ReservaModel
import com.lino.tecsupfit.repository.ReservasRepository

@Composable
fun ReservasScreen(navController: NavController) {
    val reservas = ReservasRepository.misReservas
    var reservaACancelar by remember { mutableStateOf<ReservaModel?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis reservas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(reservas, key = { it.id }) { reserva ->
                remember(reserva.estado) { reserva.estado }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(reserva.nombreClase, style = MaterialTheme.typography.titleMedium)
                        Text(reserva.horario)
                        Text("Estado: ${reserva.estado}", style = MaterialTheme.typography.bodySmall)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (reserva.estado == "Confirmada") {
                                Button(onClick = { ReservasRepository.marcarComoCompletada(reserva.id) }) {
                                    Text("Completar")
                                }
                                OutlinedButton(onClick = { reservaACancelar = reserva }) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    reservaACancelar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            title = { Text("Cancelar Reserva") },
            text = { Text("¿Deseas cancelar la reserva para ${reserva.nombreClase}?") },
            confirmButton = {
                TextButton(onClick = {
                    ReservasRepository.cancelarReserva(reserva.id)
                    reservaACancelar = null
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaACancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}