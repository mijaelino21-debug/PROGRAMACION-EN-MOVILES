package com.tecsup.catalogo.ui.detalle.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoReserva(
    hotel: Hotel,
    reservaEstado: UiState<Unit>?,
    onDismiss: () -> Unit,
    onConfirmar: (String, String, Int, Int, Double) -> Unit
) {
    val calendar = java.util.Calendar.getInstance()
    val defaultEntradaMillis = calendar.timeInMillis
    calendar.add(java.util.Calendar.DAY_OF_YEAR, 3)
    val defaultSalidaMillis = calendar.timeInMillis

    var fechaEntradaMillis by remember { mutableStateOf<Long?>(defaultEntradaMillis) }
    var fechaSalidaMillis by remember { mutableStateOf<Long?>(defaultSalidaMillis) }
    var cantidadHuespedes by remember { mutableStateOf(1) }

    var mostrarPickerEntrada by remember { mutableStateOf(false) }
    var mostrarPickerSalida by remember { mutableStateOf(false) }

    val fechaEntradaStr = millisToDateString(fechaEntradaMillis)
    val fechaSalidaStr = millisToDateString(fechaSalidaMillis)

    val cantidadNoches = if (fechaEntradaMillis != null && fechaSalidaMillis != null) {
        val diff = fechaSalidaMillis!! - fechaEntradaMillis!!
        val days = diff / (1000 * 60 * 60 * 24)
        if (days > 0) days.toInt() else 0
    } else 0

    val precioTotal = if (cantidadNoches > 0) cantidadNoches * hotel.precioNoche else 0.0
    val datosValidos = cantidadNoches > 0 && fechaEntradaStr.isNotBlank() && fechaSalidaStr.isNotBlank()

    AlertDialog(
        onDismissRequest = { if (reservaEstado !is UiState.Cargando) onDismiss() },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Reservar Hotel",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = hotel.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Fechas de estancia", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                OutlinedCard(
                    onClick = { mostrarPickerEntrada = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Text("Fecha de entrada", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(if (fechaEntradaStr.isNotEmpty()) fechaEntradaStr else "Seleccionar fecha", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                OutlinedCard(
                    onClick = { mostrarPickerSalida = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Text("Fecha de salida", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(if (fechaSalidaStr.isNotEmpty()) fechaSalidaStr else "Seleccionar fecha", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text("Cantidad de huéspedes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Huéspedes",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FilledTonalIconButton(
                                onClick = { if (cantidadHuespedes > 1) cantidadHuespedes-- },
                                enabled = cantidadHuespedes > 1,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text("-", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            }
                            Text(
                                text = cantidadHuespedes.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            FilledTonalIconButton(
                                onClick = { if (cantidadHuespedes < 10) cantidadHuespedes++ },
                                enabled = cantidadHuespedes < 10,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text("+", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Resumen de la estancia",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Hotel:", style = MaterialTheme.typography.bodyMedium)
                            Text(hotel.nombre, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Precio por noche:", style = MaterialTheme.typography.bodyMedium)
                            Text("S/ ${hotel.precioNoche}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Noches:", style = MaterialTheme.typography.bodyMedium)
                            Text("$cantidadNoches noches", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Huéspedes:", style = MaterialTheme.typography.bodyMedium)
                            Text("$cantidadHuespedes huéspedes", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Precio Total:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "S/ $precioTotal",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (!datosValidos) {
                    Text(
                        text = "⚠️ La fecha de salida debe ser posterior a la fecha de entrada.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (reservaEstado is UiState.Cargando) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmar(fechaEntradaStr, fechaSalidaStr, cantidadHuespedes, cantidadNoches, precioTotal)
                },
                enabled = datosValidos && reservaEstado !is UiState.Cargando,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Confirmar reserva",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = reservaEstado !is UiState.Cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    )

    if (mostrarPickerEntrada) {
        DatePickerModal(
            initialSelectedDateMillis = fechaEntradaMillis,
            onDateSelected = { fechaEntradaMillis = it },
            onDismiss = { mostrarPickerEntrada = false }
        )
    }

    if (mostrarPickerSalida) {
        DatePickerModal(
            initialSelectedDateMillis = fechaSalidaMillis,
            onDateSelected = { fechaSalidaMillis = it },
            onDismiss = { mostrarPickerSalida = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    initialSelectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

private fun millisToDateString(millis: Long?): String {
    if (millis == null) return ""
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}
