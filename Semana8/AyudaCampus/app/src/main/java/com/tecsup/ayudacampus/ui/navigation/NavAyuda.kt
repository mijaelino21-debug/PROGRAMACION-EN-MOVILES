package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.cliente.LoginScreen
import com.tecsup.ayudacampus.ui.cliente.MisTicketsScreen
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults

// Barra inferior del Estudiante
private val itemsEstudiante = listOf(
    Triple(Rutas.MIS_TICKETS, "Tickets", Icons.AutoMirrored.Filled.List),
    Triple(Rutas.NUEVO_TICKET, "Nuevo", Icons.Default.Add),
    Triple(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

@Composable
fun NavAyuda(factory: AyudaViewModelFactory) {
    val navController = rememberNavController()
    val sesionVm: SesionViewModel = viewModel(factory = factory)
    val perfil by sesionVm.perfilActivo.collectAsState()

    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    // Va a la pantalla principal del rol y limpia la pila
    fun irAInicio(rol: String) {
        val destino = if (rol == Rol.ADMIN.name) Rutas.PANEL else Rutas.MIS_TICKETS
        navController.navigate(destino) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    fun irA(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Rutas.MIS_TICKETS) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            if (rutaActual in itemsEstudiante.map { it.first }) {
                NavigationBar {
                    itemsEstudiante.forEach { (ruta, texto, icono) ->
                        NavigationBarItem(
                            selected = rutaActual == ruta,
                            onClick = { irA(ruta) },
                            icon = { Icon(icono, contentDescription = texto) },
                            label = { Text(texto) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ))
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(Rutas.SPLASH) {
                SplashScreen(
                    onComenzar = {
                        val p = perfil
                        // Si ya hay perfil guardado, salta el login
                        if (p == Rol.CLIENTE.name || p == Rol.ADMIN.name) {
                            irAInicio(p)
                        } else {
                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(vm = sesionVm, onLoginExitoso = { rol -> irAInicio(rol) })
            }

            composable(Rutas.MIS_TICKETS) {
                val vm: MisTicketsViewModel = viewModel(factory = factory)
                MisTicketsScreen(
                    vm = vm,
                    onAbrirTicket = { id -> navController.navigate(Rutas.detalleTicket(id)) },
                    onNuevo = { irA(Rutas.NUEVO_TICKET) }
                )
            }

            // Pantallas temporales
            composable(Rutas.NUEVO_TICKET) { Pendiente("Nuevo ticket") }
            composable(Rutas.DETALLE_TICKET) { Pendiente("Detalle del ticket") }
            composable(Rutas.PERFIL) { Pendiente("Perfil") }
            composable(Rutas.PANEL) { Pendiente("Panel de soporte") }

            composable(Rutas.CATEGORIAS) {
                val vm: CategoriasViewModel = viewModel(factory = factory)
                CategoriasScreen(vm = vm, onEditar = { })
            }
        }
    }
}

@Composable
private fun Pendiente(texto: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(texto)
    }
}