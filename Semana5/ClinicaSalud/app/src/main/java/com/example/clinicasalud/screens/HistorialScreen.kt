package com.example.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class HistorialCita(
    val id: Int,
    val doctor: String,
    val especialidad: String,
    val fecha: String,
    val diagnostico: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(onMenuClick: () -> Unit) {
    val historialList = listOf(
        HistorialCita(1, "Dr. Carlos Mendoza", "Cardiología", "10 Feb 2025", "Chequeo preventivo OK"),
        HistorialCita(2, "Dra. Ana Torres", "Pediatría", "15 Ene 2025", "Control de rutina"),
        HistorialCita(3, "Dr. Roberto Gómez", "Dermatología", "02 Dic 2024", "Tratamiento piel finalizado")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial médico") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historialList) { cita ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = cita.doctor, style = MaterialTheme.typography.titleMedium)
                            Text(text = "${cita.especialidad} • ${cita.fecha}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Diagnóstico: ${cita.diagnostico}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}