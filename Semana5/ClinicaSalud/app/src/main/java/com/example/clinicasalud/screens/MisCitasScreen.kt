package com.example.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clinicasalud.model.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(navController: NavController, onMenuClick: () -> Unit, citas: List<Cita>) {
    // Scaffold provee la estructura básica con la barra superior (TopAppBar)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                // Botón que ejecuta la función del menú lateral
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { pad ->
        // Contenedor principal que respeta los márgenes del Scaffold
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Si la lista  no tiene elementos, se muestra el siguiente mensaje
            if (citas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tienes citas agendadas aún.")
                }
            } else {
                // Renderiza eficientemente las tarjetas de las citas registradas
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(citas) { cita ->
                        // 1. Declaramos el estado local al inicio de cada tarjeta
                        val estadoActual = remember { mutableStateOf(cita.estado) }

                        // TARJETA DE LA CITA:
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Datos de la cita: Nombre del médico y fecha/hora
                                Text(
                                    text = cita.medico.nombre,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${cita.fecha}, ${cita.hora}",
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // acciones y estado
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Cambia de color verde (Confirmada) a gris (Completada) leyendo 'estadoActual.value'
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(estadoActual.value) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = if (estadoActual.value == "Confirmada") Color(0xFFE8F5E9) else Color(0xFFEEEEEE),
                                            labelColor = if (estadoActual.value == "Confirmada") Color(0xFF2E7D32) else Color.DarkGray
                                        )
                                    )

                                    // Se muestra únicamente si la cita sigue en estado "Confirmada"
                                    if (estadoActual.value == "Confirmada") {
                                        TextButton(onClick = {
                                            cita.estado = "Completada"
                                            estadoActual.value = "Completada"
                                        }) {
                                            Text("Marcar completada")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}