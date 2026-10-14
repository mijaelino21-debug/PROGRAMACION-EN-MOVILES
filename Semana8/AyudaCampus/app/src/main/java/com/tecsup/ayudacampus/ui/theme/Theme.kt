package com.tecsup.ayudacampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.ayudacampus.model.Rol

// Entrega un ColorScheme segun el perfil: Estudiante = cian, Soporte = violeta.
// En las pantallas usa MaterialTheme.colorScheme.primary, nunca un color fijo.
@Composable
fun AyudaTheme(
    rol: Rol = Rol.CLIENTE,
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val soporte = rol == Rol.ADMIN

    val esquema = if (modoOscuro) {
        darkColorScheme(
            primary = if (soporte) Color(0xFFC4B5FD) else Color(0xFF67E8F9),
            onPrimary = Color(0xFF111827),
            primaryContainer = if (soporte) Color(0xFF4C1D95) else Color(0xFF164E63),
            onPrimaryContainer = Color(0xFFF3F4F6),
            background = Color(0xFF0F172A),
            onBackground = Color(0xFFF3F4F6),
            surface = Color(0xFF1E293B),
            onSurface = Color(0xFFF3F4F6),
            onSurfaceVariant = Color(0xFFCBD5E1),
            outlineVariant = Color(0xFF475569)
        )
    } else {
        lightColorScheme(
            primary = if (soporte) VioletaPrimario else CianPrimario,
            onPrimary = Color.White,
            primaryContainer = if (soporte) VioletaContenedor else CianContenedor,
            onPrimaryContainer = if (soporte) Color(0xFF3B0764) else Color(0xFF083344),
            background = if (soporte) FondoSoporte else FondoCliente,
            onBackground = Color(0xFF111827),
            surface = Color.White,
            onSurface = Color(0xFF111827),
            onSurfaceVariant = Color(0xFF6B7280),
            outlineVariant = Color(0xFFD1D5DB)
        )
    }

    MaterialTheme(
        colorScheme = esquema,
        typography = Typography,
        content = content
    )
}