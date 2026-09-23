package com.example.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Menú") }
                }
            )
        }
    ) { pad ->
        Column(modifier = Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            if (citas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes citas agendadas aún.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(citas) { cita ->
                        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(cita.medico.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text("${cita.fecha}, ${cita.hora}", color = Color.Gray)
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(cita.estado) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = if (cita.estado == "Confirmada") Color(0xFFE8F5E9) else Color(0xFFEEEEEE),
                                            labelColor = if (cita.estado == "Confirmada") Color(0xFF2E7D32) else Color.DarkGray
                                        )
                                    )
                                    if (cita.estado == "Confirmada") {
                                        TextButton(onClick = { cita.estado = "Completada" }) {
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