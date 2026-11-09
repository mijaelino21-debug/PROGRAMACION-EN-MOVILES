package com.tecsup.catalogo.ui.reservas.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Reserva

@Composable
fun DetalleReservaDialog(reserva: Reserva, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Detalles de la Reserva", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Hotel: ${reserva.nombreHotel}", fontWeight = FontWeight.Bold)
                if (reserva.ciudad.isNotEmpty()) Text("Ubicación: ${reserva.ciudad}")
                Text("Estado: ${reserva.estado}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text("Fecha de entrada: ${reserva.fechaEntrada}")
                Text("Fecha de salida: ${reserva.fechaSalida}")
                Text("Duración: ${reserva.cantidadNoches} noches")
                Text("Huéspedes: ${reserva.cantidadHuespedes}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text("Precio por noche: S/ ${reserva.precioPorNoche}")
                Text(
                    text = "Precio Total: S/ ${reserva.precioTotal}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                Text("Cerrar")
            }
        }
    )
}
