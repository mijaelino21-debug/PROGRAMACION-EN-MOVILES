package com.example.clinicasalud.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MisCitas : Screen("mis_citas")
    object Historial : Screen("historial")

    object PerfilMedico : Screen("perfil/{medicoId}") {
        fun createRoute(medicoId: Int) = "perfil/$medicoId"
    }
    object AgendarCita : Screen("agendar/{medicoId}") {
        fun createRoute(medicoId: Int) = "agendar/$medicoId"
    }
    object Confirmacion : Screen("confirmacion/{medicoId}/{fecha}/{hora}") {
        fun createRoute(medicoId: Int, fecha: String, hora: String) =
            "confirmacion/$medicoId/$fecha/$hora"
    }
}