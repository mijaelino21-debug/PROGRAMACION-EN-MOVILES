package com.tecsup.lino.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    destinoActual: String,
    cantidadFavoritos: Int,
    onNavegarA: (String) -> Unit
) {
    ModalDrawerSheet {
        // Encabezado del usuario
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "MR",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Maria Rojas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "maria@tecsup.edu.pe",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        // Opciones de navegación
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = destinoActual == "inicio",
            onClick = { onNavegarA("inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = destinoActual == "pedidos",
            onClick = { onNavegarA("pedidos") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // Item Favoritos con Badge contador
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = destinoActual == "favoritos",
            onClick = { onNavegarA("favoritos") },
            icon = {
                BadgedBox(
                    badge = {
                        if (cantidadFavoritos > 0) {
                            Badge { Text(cantidadFavoritos.toString()) }
                        }
                    }
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = destinoActual == "perfil",
            onClick = { onNavegarA("perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = { onNavegarA("login") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}