package com.tecsup.catalogo.ui.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun HotelFormDialog(
    hotelInicial: Hotel?,
    onDismiss: () -> Unit,
    onGuardar: (Hotel) -> Unit
) {
    var nombre by remember { mutableStateOf(hotelInicial?.nombre ?: "") }
    var ciudad by remember { mutableStateOf(hotelInicial?.ciudad ?: "") }
    var descripcion by remember { mutableStateOf(hotelInicial?.descripcion ?: "") }
    var precioNocheStr by remember { mutableStateOf(hotelInicial?.precioNoche?.toString() ?: "") }
    var estrellasStr by remember { mutableStateOf(hotelInicial?.estrellas?.toString() ?: "4") }
    var imagenUrl by remember { mutableStateOf(hotelInicial?.imagenUrl ?: "") }
    var serviciosStr by remember { mutableStateOf(hotelInicial?.servicios?.joinToString(", ") ?: "Wi-Fi gratis, Piscina, Aire acondicionado") }
    var activo by remember { mutableStateOf(hotelInicial?.activo ?: true) }

    val esValido = nombre.isNotBlank() && ciudad.isNotBlank() && precioNocheStr.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (hotelInicial == null) "Agregar Nuevo Hotel" else "Editar Hotel", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del hotel") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = { Text("Ciudad / Ubicación") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = precioNocheStr,
                    onValueChange = { precioNocheStr = it },
                    label = { Text("Precio por noche (S/)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = estrellasStr,
                    onValueChange = { estrellasStr = it },
                    label = { Text("Calificación (1 - 5 estrellas)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de imagen") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = serviciosStr,
                    onValueChange = { serviciosStr = it },
                    label = { Text("Servicios (separados por coma)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hotel Activo / Disponible", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = activo, onCheckedChange = { activo = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val precio = precioNocheStr.toDoubleOrNull() ?: 0.0
                    val estrellas = estrellasStr.toIntOrNull() ?: 3
                    val serviciosList = serviciosStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    val hotel = Hotel(
                        id = hotelInicial?.id ?: 0,
                        nombre = nombre.trim(),
                        descripcion = descripcion.trim(),
                        ciudad = ciudad.trim(),
                        precioNoche = precio,
                        estrellas = estrellas,
                        imagenUrl = imagenUrl.trim().ifEmpty { "https://picsum.photos/300/200" },
                        servicios = if (serviciosList.isNotEmpty()) serviciosList else listOf("Wi-Fi gratis", "Piscina"),
                        activo = activo
                    )
                    onGuardar(hotel)
                },
                enabled = esValido,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
