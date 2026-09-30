package com.tecsup.lino.navigation

sealed class Screen(val route: String, val title: String) {
    object Inicio : Screen("inicio", "Inicio")
    object Pedidos : Screen("pedidos", "Mis pedidos")
    object Favoritos : Screen("favoritos", "Favoritos")
    object Perfil : Screen("perfil", "Perfil")
}