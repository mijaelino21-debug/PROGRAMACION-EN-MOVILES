package com.tecsup.catalogo.ui.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Reserva

@Composable
fun DetalleGestionReservaDialog(
    reserva: Reserva,
    onDismiss: () -> Unit,
    onConfirmarClick: () -> Unit,
    onRechazarClick: () -> Unit
) {
    val esPendiente = reserva.estado == "PENDIENTE"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gestionar Reserva #${reserva.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Hotel: ${reserva.nombreHotel}", fontWeight = FontWeight.Bold)
                if (reserva.ciudad.isNotEmpty()) Text("Ubicación: ${reserva.ciudad}")
                Text("Usuario UID: ${reserva.usuarioId}")
                Text("Estado actual: ${reserva.estado}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text("Fechas: ${reserva.fechaEntrada} al ${reserva.fechaSalida} (${reserva.cantidadNoches} noches)")
                Text("Huéspedes: ${reserva.cantidadHuespedes}")
                Text("Precio por noche: S/ ${reserva.precioPorNoche}")
                Text("Precio Total: S/ ${reserva.precioTotal}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                if (reserva.motivoRechazo.isNotBlank()) {
                    Text("Motivo de rechazo: ${reserva.motivoRechazo}", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (esPendiente) {
                    OutlinedButton(
                        onClick = onRechazarClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Rechazar")
                    }
                    Button(
                        onClick = onConfirmarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF36A6C5)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Confirmar reserva")
                    }
                } else {
                    Button(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cerrar")
                    }
                }
            }
        },
        dismissButton = {
            if (esPendiente) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}
