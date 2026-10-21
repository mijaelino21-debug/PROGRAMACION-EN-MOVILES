package com.tecsup.ayudacampus.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.EstadoTicket

@Composable
fun StepperTicket(estado: EstadoTicket, modifier: Modifier = Modifier) {
    val pasos = EstadoTicket.entries
    val actual = estado.ordinal
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outlineVariant

    Row(modifier = modifier.fillMaxWidth()) {
        pasos.forEachIndexed { i, paso ->
            val esActivoOCompletado = i <= actual
            val esActual = i == actual

            val colorLinea by animateColorAsState(
                targetValue = if (i <= actual) activo else inactivo,
                animationSpec = tween(500),
                label = "colorLinea"
            )

            val colorCirculo by animateColorAsState(
                targetValue = if (esActivoOCompletado) activo else inactivo,
                animationSpec = tween(400),
                label = "colorCirculo"
            )

            val escalaCirculo by animateFloatAsState(
                targetValue = if (esActual) 1.15f else 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
                label = "escalaCirculo"
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.5.dp)
                            .background(if (i == 0) Color.Transparent else colorLinea)
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .scale(escalaCirculo)
                            .background(colorCirculo, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (i < actual) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        } else if (esActual) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MaterialTheme.colorScheme.onPrimary, CircleShape)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.5.dp)
                            .background(if (i == pasos.lastIndex) Color.Transparent else if (i < actual) activo else inactivo)
                    )
                }
                val textColor by animateColorAsState(
                    targetValue = if (esActivoOCompletado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(300),
                    label = "textColor"
                )
                Text(
                    text = paso.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
            }
        }
    }
}
