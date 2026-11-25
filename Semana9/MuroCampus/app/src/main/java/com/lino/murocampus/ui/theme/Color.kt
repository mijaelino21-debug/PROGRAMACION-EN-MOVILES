package com.lino.murocampus.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val AzulPrimario = Color(0xFF1B6CA8)
val FondoPantalla = Color(0xFFFCFAFF)
val FondoPost = Color(0xFFF0F3F8)
val AmbarUsuario = Color(0xFFF2A900)
val BordeClaro = Color(0xFFD6DCE5)
val TextoGris = Color(0xFF5F6B7A)
val VerdeMuyClaro = Color(0xFFF0FAF2)
val VerdeExito = Color(0xFF1B5E20)

private val PaletaUsuarios = listOf(
    Color(0xFF2E7D32),
    Color(0xFF6A1B9A),
    Color(0xFF00695C),
    Color(0xFFAD1457),
    Color(0xFF4527A0),
    Color(0xFF37474F)
)

fun colorUsuario(userId: Int): Color = when (userId) {
    1 -> AzulPrimario
    2 -> AmbarUsuario
    else -> PaletaUsuarios[((userId % PaletaUsuarios.size) + PaletaUsuarios.size) % PaletaUsuarios.size]
}

fun colorTextoUsuario(userId: Int): Color =
    if (userId == 2) Color(0xFF1C1B1F) else Color.White
