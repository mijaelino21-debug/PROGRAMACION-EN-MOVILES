package com.lino.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RutinasScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Rutinas Recomendadas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("1. Hipertrofia Tren Superior (45 min)", modifier = Modifier.padding(16.dp))
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Text("2. Cardio HIIT & Core (30 min)", modifier = Modifier.padding(16.dp))
        }
    }
}