package com.lino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lino.tecsupfit.navigation.Screen
import com.lino.tecsupfit.repository.ReservasRepository

@Composable
fun ConfirmacionScreen(navController: NavController, claseId: String, horario: String) {
    val clase = ReservasRepository.clasesDisponibles.find { it.id == claseId }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✓", style = MaterialTheme.typography.displayLarge)
        Text("¡Cupo reservado!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(clase?.nombre ?: "", style = MaterialTheme.typography.titleLarge)
        Text("Horario: $horario | ${clase?.sala ?: ""}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Reservas.route) {
                    popUpTo(Screen.Inicio.route)
                }
            }
        ) {
            Text("Ver mis reservas")
        }
    }
}