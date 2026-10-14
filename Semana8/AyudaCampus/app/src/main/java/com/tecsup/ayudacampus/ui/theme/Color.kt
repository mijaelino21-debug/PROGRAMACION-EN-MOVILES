package com.tecsup.ayudacampus.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad

// Estudiante (cian)
val CianPrimario = Color(0xFF0891B2)
val CianContenedor = Color(0xFFCFFAFE)
val FondoCliente = Color(0xFFF7FDFE)

// Soporte TI (violeta)
val VioletaPrimario = Color(0xFF7C3AED)
val VioletaContenedor = Color(0xFFEDE4FF)
val FondoSoporte = Color(0xFFFAF8FF)

// Estado del ticket
val ColorAbierto = Color(0xFFD97706)
val ColorEnProceso = Color(0xFF2563EB)
val ColorResuelto = Color(0xFF16A34A)
val ColorCerrado = Color(0xFF6B7280)

// Prioridad
val ColorAlta = Color(0xFFDC2626)
val ColorMedia = Color(0xFFB45309)
val ColorBaja = Color(0xFF6B7280)

fun EstadoTicket.color(): Color = when (this) {
    EstadoTicket.ABIERTO -> ColorAbierto
    EstadoTicket.EN_PROCESO -> ColorEnProceso
    EstadoTicket.RESUELTO -> ColorResuelto
    EstadoTicket.CERRADO -> ColorCerrado
}

fun Prioridad.color(): Color = when (this) {
    Prioridad.ALTA -> ColorAlta
    Prioridad.MEDIA -> ColorMedia
    Prioridad.BAJA -> ColorBaja
}