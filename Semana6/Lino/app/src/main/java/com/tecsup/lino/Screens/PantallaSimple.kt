package com.tecsup.lino.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tecsup.lino.navigation.BarraTienda

@Composable
fun PantallaSimple(titulo: String, onMenuClick: () -> Unit) {
    Scaffold(
        topBar = { BarraTienda(titulo = titulo, onMenuClick = onMenuClick) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = titulo, style = MaterialTheme.typography.headlineMedium)
        }
    }
}