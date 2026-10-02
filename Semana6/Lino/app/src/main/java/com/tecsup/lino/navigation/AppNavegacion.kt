package com.tecsup.lino.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.tecsup.lino.Screens.PantallaSimple
import com.tecsup.lino.PantallaCarrito
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoActual by rememberSaveable { mutableStateOf("inicio") }
    var contadorFavoritos by rememberSaveable { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                destinoActual = destinoActual,
                cantidadFavoritos = contadorFavoritos,
                onNavegarA = { nuevoDestino ->
                    destinoActual = nuevoDestino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Surface(modifier = Modifier.padding(innerPadding)) {
                when (destinoActual) {
                    "inicio" -> PantallaCarrito(
                        onAgregarFavorito = { contadorFavoritos++ }
                    )
                    "pedidos" -> PantallaSimple(titulo = "Mis Pedidos")
                    "favoritos" -> PantallaSimple(titulo = "Productos Favoritos ($contadorFavoritos)")
                    "perfil" -> PantallaSimple(titulo = "Mi Perfil")
                }
            }
        }
    }
}