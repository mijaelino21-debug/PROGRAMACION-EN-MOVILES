package com.tecsup.catalogo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AvisoSinConexion(hayConexion: Boolean) {
    AnimatedVisibility(
        visible = !hayConexion,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD32F2F))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Modo sin conexión: mostrando datos locales",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}