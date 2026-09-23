package com.lino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lino.tecsupfit.navigation.Screen
import com.lino.tecsupfit.repository.ReservasRepository

@Composable
fun DetalleClaseScreen(navController: NavController, claseId: String) {
    val clase = ReservasRepository.clasesDisponibles.find { it.id == claseId }

    if (clase == null) {
        Text("Clase no encontrada")
        return
    }

    val horariosDisponibles = listOf("7:00 am", "6:00 pm", "7:30 pm")
    var horarioSeleccionado by remember { mutableStateOf(horariosDisponibles.first()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("← Detalle de clase", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text(clase.nombre, style = MaterialTheme.typography.headlineMedium)
        Text("${clase.horario} - ${clase.sala} (${clase.duracion})")
        Spacer(modifier = Modifier.height(8.dp))
        Text(clase.descripcion)
        Text("${clase.cuposDisponibles} de ${clase.totalCupos} cupos disponibles")

        Spacer(modifier = Modifier.height(24.dp))
        Text("Selecciona Horario:", style = MaterialTheme.typography.titleMedium)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(horariosDisponibles) { hor ->
                FilterChip(
                    selected = horarioSeleccionado == hor,
                    onClick = { horarioSeleccionado = hor },
                    label = { Text(hor) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                ReservasRepository.agregarReserva(clase, horarioSeleccionado)
                navController.navigate(Screen.Confirmacion.createRoute(clase.id, horarioSeleccionado))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}