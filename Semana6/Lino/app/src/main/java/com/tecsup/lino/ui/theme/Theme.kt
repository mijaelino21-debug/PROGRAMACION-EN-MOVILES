package com.tecsup.lino.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleSecondary,
    background = PurpleBackground,
    surface = Color.White,
    surfaceVariant = CardBackground,
    onPrimary = Color.White
)

@Composable
fun LinoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}