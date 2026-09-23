package com.lino.tecsupfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                NavigationBar(
                    containerColor = Color.White
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
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
                            label = { Text(item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF00695C),
                                selectedTextColor = Color(0xFF00695C),
                                indicatorColor = Color(0xFFE0F2F1),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
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
            composable(Screen.Reservas.route) { ReservasScreen() }

            // AQUÍ: La pestaña Rutinas ahora carga la pantalla de IA
            composable(Screen.Rutinas.route) { IAScreen() }

            composable(Screen.Perfil.route) { PerfilScreen() }

            composable(Screen.DetalleClase.route) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getString("claseId") ?: ""
                DetalleClaseScreen(navController, claseId)
            }

            composable(Screen.Confirmacion.route) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getString("claseId") ?: ""
                ConfirmacionScreen(navController, claseId)
            }
        }
    }
}