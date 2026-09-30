package com.tecsup.lino.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.tecsup.lino.PantallaCarrito
import com.tecsup.lino.screens.PantallaSimple
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Inicio.route)
                        launchSingleTop = true
                    }
                },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Inicio.route) {
            composable(Screen.Inicio.route) {
                PantallaCarrito(onMenuClick = { scope.launch { drawerState.open() } })
            }
            composable(Screen.Pedidos.route) {
                PantallaSimple(titulo = Screen.Pedidos.title, onMenuClick = { scope.launch { drawerState.open() } })
            }
            composable(Screen.Favoritos.route) {
                PantallaSimple(titulo = Screen.Favoritos.title, onMenuClick = { scope.launch { drawerState.open() } })
            }
            composable(Screen.Perfil.route) {
                PantallaSimple(titulo = Screen.Perfil.title, onMenuClick = { scope.launch { drawerState.open() } })
            }
        }
    }
}