package com.tecsup.lino.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("ML", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Mijael Lino", style = MaterialTheme.typography.titleMedium)
            Text("mijaelino21@gmail.com", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(12.dp))

        val items = listOf(
            Screen.Inicio to Icons.Default.Home,
            Screen.Pedidos to Icons.Default.ShoppingCart,
            Screen.Favoritos to Icons.Default.Favorite,
            Screen.Perfil to Icons.Default.Person
        )

        items.forEach { (screen, icon) ->
            NavigationDrawerItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    onNavigate(screen)
                    onCloseDrawer()
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = { onCloseDrawer() },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}