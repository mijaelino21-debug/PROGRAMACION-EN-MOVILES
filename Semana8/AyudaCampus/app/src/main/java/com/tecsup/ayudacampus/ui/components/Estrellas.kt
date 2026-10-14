package com.tecsup.ayudacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Estrellas(
    valor: Int,
    modifier: Modifier = Modifier,
    onCambio: ((Int) -> Unit)? = null
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val icono = if (i <= valor) Icons.Filled.Star else Icons.Outlined.StarBorder
            val clic = if (onCambio != null) Modifier.clickable { onCambio(i) } else Modifier
            Icon(
                imageVector = icono,
                contentDescription = "$i estrellas",
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(36.dp).then(clic)
            )
        }
    }
}