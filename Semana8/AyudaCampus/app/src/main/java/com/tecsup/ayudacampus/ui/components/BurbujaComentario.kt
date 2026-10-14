package com.tecsup.ayudacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BurbujaComentario(
    autor: String,
    texto: String,
    hora: String,
    propio: Boolean,
    modifier: Modifier = Modifier
) {
    val fondo = if (propio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val contenido = if (propio) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (propio) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = fondo,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "$autor · $hora",
                    style = MaterialTheme.typography.labelSmall,
                    color = contenido.copy(alpha = 0.8f)
                )
                Text(
                    text = texto,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contenido
                )
            }
        }
    }
}