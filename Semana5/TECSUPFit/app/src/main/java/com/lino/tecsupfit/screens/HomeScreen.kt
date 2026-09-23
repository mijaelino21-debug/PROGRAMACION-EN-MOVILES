package com.lino.tecsupfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun HomeScreen(navController: NavController) {
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }
    val categorias = listOf("Hoy", "Esta semana")

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = androidx.compose.ui.graphics.Color(0xFF00695C),
            modifier = Modifier.fillMaxWidth()
        ){
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 20.dp)) {
                Text("TECSUP Fit", style = MaterialTheme.typography.headlineLarge, color = androidx.compose.ui.graphics.Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Hola, Mijael", style = MaterialTheme.typography.bodyMedium, color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f))
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categorias) { cat ->
                        FilterChip(
                            selected = filtroSeleccionado == cat,
                            onClick = { filtroSeleccionado = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = androidx.compose.ui.graphics.Color(0xFF00695C),
                                selectedLabelColor = androidx.compose.ui.graphics.Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Clases disponibles", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                val clasesFiltradas = ReservasRepository.clasesDisponibles.filter { it.categoria == filtroSeleccionado }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(clasesFiltradas) { clase ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                navController.navigate(Screen.DetalleClase.createRoute(clase.id))
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(clase.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("${clase.horario} - ${clase.sala}")
                            }
                        }
                    }
                }
            }
        }
}