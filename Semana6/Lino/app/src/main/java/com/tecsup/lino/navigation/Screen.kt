package com.tecsup.lino.navigation

// Igual que en la Sem 5: una clase sellada con las rutas de la app
sealed class Screen(val route: String) {
    object Inicio : Screen(route = "inicio")
}