package com.lino.navlab.navigation

// Clase sellada que actúa como contrato central de navegación.
// Recibe "route" como parámetro - es el identificador único de cada pantalla.
// Al ser sealed, el compilador conoce todas las rutas posibles en tiempo de compilación.
sealed class Screen(val route: String) {
    object Login : Screen(route = "login")
    object Home : Screen(route = "home/{userName}") {
        fun createRoute(userName: String): String = "home/$userName"
    }
    object List : Screen(route = "list")
    object Profile : Screen(route = "profile")
    object Detail : Screen(route = "detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}