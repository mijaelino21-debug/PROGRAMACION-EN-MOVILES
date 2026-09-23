package com.example.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clinicasalud.model.Medico
import com.example.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(navController: NavController, medico: Medico) {
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00 am", "10:30 am", "4:00 pm")

    var fechaSel by remember { mutableStateOf("") }
    var horaSel by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Selecciona una fecha", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(fechas) { f ->
                        FilterChip(selected = (fechaSel == f), onClick = { fechaSel = f }, label = { Text(f) })
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Selecciona una hora", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horas) { h ->
                        FilterChip(selected = (horaSel == h), onClick = { horaSel = h }, label = { Text(h) })
                    }
                }
            }
            Button(
                onClick = { navController.navigate(Screen.Confirmacion.createRoute(medico.id, fechaSel, horaSel)) },
                enabled = fechaSel.isNotEmpty() && horaSel.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2E83))
            ) { Text("Confirma la  cita", color = Color.White) }
        }
    }
}