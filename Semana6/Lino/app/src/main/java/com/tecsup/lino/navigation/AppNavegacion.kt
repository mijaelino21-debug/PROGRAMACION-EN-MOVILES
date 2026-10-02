package com.tecsup.lino.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tecsup.lino.Producto
import com.tecsup.lino.PantallaCarrito
import com.tecsup.lino.Screens.PantallaFavoritos
import com.tecsup.lino.Screens.PantallaSimple
import com.tecsup.lino.ui.theme.LinoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    LinoTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var destinoActual by rememberSaveable { mutableStateOf("inicio") }

        // Productos precargados tal como en el PDF
        val listaProductos = remember {
            mutableStateListOf(
                Producto("Audífonos", 89.0, 1),
                Producto("Smartwatch", 199.0, 1),
                Producto("Funda celular", 25.0, 1)
            )
        }
        val listaFavoritos = remember { mutableStateListOf<Producto>() }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    destinoActual = destinoActual,
                    cantidadFavoritos = listaFavoritos.size,
                    onNavegarA = { nuevoDestino ->
                        destinoActual = nuevoDestino
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "TECSUP Store",
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menú",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            ) { innerPadding ->
                Surface(modifier = Modifier.padding(innerPadding)) {
                    when (destinoActual) {
                        "inicio" -> PantallaCarrito(
                            listaProductos = listaProductos,
                            listaFavoritos = listaFavoritos,
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                        "pedidos" -> PantallaSimple(
                            titulo = "Mis Pedidos",
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                        "favoritos" -> PantallaFavoritos(
                            listaFavoritos = listaFavoritos,
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                        "perfil" -> PantallaSimple(
                            titulo = "Mi Perfil",
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        }
    }
}