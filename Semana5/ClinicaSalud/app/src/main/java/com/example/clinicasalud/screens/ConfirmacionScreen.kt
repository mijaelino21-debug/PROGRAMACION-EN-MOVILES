package com.example.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clinicasalud.model.Medico
import com.example.clinicasalud.navigation.Screen

@Composable
fun ConfirmacionScreen(navController: NavController, medico: Medico, fecha: String, hora: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(90.dp), tint = Color(0xFF4CAF50))
        Spacer(Modifier.height(16.dp))
        Text("¡Tu Cita fue agendada!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(medico.nombre, style = MaterialTheme.typography.titleMedium)
        Text("$fecha, $hora", color = Color.Gray)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                navController.navigate(Screen.MisCitas.route) {
                    popUpTo(Screen.Home.route) { inclusive = false }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2E83))
        ) { Text("ver todas mis citas", color = Color.White) }
    }
}