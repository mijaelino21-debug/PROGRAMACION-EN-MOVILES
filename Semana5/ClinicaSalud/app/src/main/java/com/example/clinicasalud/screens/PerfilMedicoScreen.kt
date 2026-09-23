package com.example.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMedicoScreen(navController: NavController, medico: Medico) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("perfil del medico") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "atras")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(90.dp), tint = Color(0xFF4A2E83))
                Spacer(Modifier.height(12.dp))
                Text(medico.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(medico.especialidad, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107))
                    Text(" ${medico.calificacion}", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    "Especialista en arritmias e hipertensión, formación en la Clínica Solidaridad.",
                    color = Color.DarkGray
                )
            }
            Button(
                onClick = { navController.navigate(Screen.AgendarCita.createRoute(medico.id)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2E83))
            ) { Text("agendar una  cita", color = Color.White) }
        }
    }
}