package com.tecsup.lino.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    onNavigate: (Screen) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Text("TECSUP Store", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = false,
            onClick = { onNavigate(Screen.Inicio); onCloseDrawer() }
        )
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = false,
            onClick = { onNavigate(Screen.Pedidos); onCloseDrawer() }
        )
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = false,
            onClick = { onNavigate(Screen.Favoritos); onCloseDrawer() }
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = false,
            onClick = { onNavigate(Screen.Perfil); onCloseDrawer() }
        )
    }
}