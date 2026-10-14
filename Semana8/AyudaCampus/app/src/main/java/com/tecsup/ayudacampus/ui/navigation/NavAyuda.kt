package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel

@Composable
fun NavAyuda(factory: AyudaViewModelFactory) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            SplashScreen(
                onComenzar = {
                    navController.navigate(Rutas.CATEGORIAS) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.CATEGORIAS) {
            val vm: CategoriasViewModel = viewModel(factory = factory)
            CategoriasScreen(vm = vm, onEditar = { /* TODO Paso 11 */ })
        }

        // TODO Paso 6: Login
        // TODO Pasos 7-9: Estudiante
        // TODO Pasos 10-11: Soporte
        // TODO Paso 12: Perfil, Ajustes y cambio de perfil
    }
}