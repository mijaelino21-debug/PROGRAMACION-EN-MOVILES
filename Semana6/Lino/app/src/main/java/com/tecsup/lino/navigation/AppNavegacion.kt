package com.tecsup.lino.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.tecsup.lino.Producto
import com.tecsup.lino.screens.PantallaCarrito
import com.tecsup.lino.screens.PantallaFavoritos
import com.tecsup.lino.screens.PantallaSimple
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Listas compartidas globales
    val listaProductos = remember {
        mutableStateListOf(
            Producto("Laptop Gaming", 3500.0, 1),
            Producto("Mouse Inalámbrico", 80.0, 2)
        )
    }
    val listaFavoritos = remember { mutableStateListOf<Producto>() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
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
                PantallaCarrito(
                    listaProductos = listaProductos,
                    listaFavoritos = listaFavoritos,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(Screen.Pedidos.route) {
                PantallaSimple(titulo = Screen.Pedidos.title, onMenuClick = { scope.launch { drawerState.open() } })
            }
            composable(Screen.Favoritos.route) {
                PantallaFavoritos(
                    listaFavoritos = listaFavoritos,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(Screen.Perfil.route) {
                PantallaSimple(titulo = Screen.Perfil.title, onMenuClick = { scope.launch { drawerState.open() } })
            }
        }
    }
}