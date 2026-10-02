package com.tecsup.lino.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(
    destinoActual: String,
    cantidadFavoritos: Int,
    onNavegarA: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar con tus iniciales
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ML", // Pon aquí tus iniciales (ejemplo: Mijael Lino)
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Mijael Lino", // Pon aquí tu nombre completo
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "mijael.lino@tecsup.edu.pe", // Pon aquí tu correo institucional
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(12.dp))

        // --- OPCIONES DE NAVEGACIÓN ---
        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            selected = destinoActual == "inicio",
            onClick = { onNavegarA("inicio") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
            selected = destinoActual == "pedidos",
            onClick = { onNavegarA("pedidos") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        )

        // Opción Favoritos con BadgedBox para el contador reactivo
        NavigationDrawerItem(
            label = { Text("Favoritos") },
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
            selected = destinoActual == "favoritos",
            onClick = { onNavegarA("favoritos") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            selected = destinoActual == "perfil",
            onClick = { onNavegarA("perfil") },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            selected = false,
            onClick = { /* Acción de salir */ },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
        )
    }
}