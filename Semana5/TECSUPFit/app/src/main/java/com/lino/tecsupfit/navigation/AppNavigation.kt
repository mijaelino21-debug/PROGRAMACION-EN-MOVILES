package com.lino.tecsupfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.lino.tecsupfit.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon!!, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) { HomeScreen(navController) }
            composable(Screen.Reservas.route) { ReservasScreen(navController) }
            composable(Screen.Rutinas.route) { RutinasScreen() }
            composable(Screen.Perfil.route) { PerfilScreen() }

            composable(Screen.DetalleClase.route) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getString("claseId") ?: ""
                DetalleClaseScreen(navController, claseId)
            }

            composable(Screen.Confirmacion.route) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getString("claseId") ?: ""
                val horario = backStackEntry.arguments?.getString("horario") ?: ""
                ConfirmacionScreen(navController, claseId, horario)
            }
        }
    }
}