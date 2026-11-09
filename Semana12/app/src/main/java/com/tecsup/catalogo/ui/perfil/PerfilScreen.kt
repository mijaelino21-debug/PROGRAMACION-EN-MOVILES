package com.tecsup.catalogo.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.ui.perfil.components.PerfilHeader
import com.tecsup.catalogo.ui.perfil.components.PerfilMenuCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    userEmail: String,
    isAdmin: Boolean,
    onIrAlAdmin: () -> Unit,
    onVerMisReservas: () -> Unit,
    onVerMisFavoritos: () -> Unit,
    onIrAGestionHoteles: () -> Unit = {},
    onIrAGestionReservas: () -> Unit = {},
    onCerrarSesion: () -> Unit
) {
    val displayName = userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Cuenta", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PerfilHeader(
                displayName = displayName,
                isAdmin = isAdmin
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                )
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (!isAdmin) {
                    Text(
                        text = "Mis Viajes y Favoritos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    PerfilMenuCard(
                        titulo = "Mis Reservas",
                        subtitulo = "Gestiona tus estancias pasadas y próximas",
                        icon = Icons.Outlined.EventNote,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = onVerMisReservas
                    )

                    PerfilMenuCard(
                        titulo = "Mis Favoritos",
                        subtitulo = "Hoteles guardados para tus próximas vacaciones",
                        icon = Icons.Default.Favorite,
                        iconTint = MaterialTheme.colorScheme.error,
                        onClick = onVerMisFavoritos
                    )
                } else {
                    Text(
                        text = "Gestión del Sistema",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    PerfilMenuCard(
                        titulo = "Panel Administrativo",
                        subtitulo = "Estadísticas y control general de la app",
                        icon = Icons.Default.AdminPanelSettings,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = onIrAlAdmin
                    )

                    PerfilMenuCard(
                        titulo = "Gestión de Hoteles",
                        subtitulo = "Agregar, editar, activar o eliminar hoteles",
                        icon = Icons.Default.Hotel,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        onClick = onIrAGestionHoteles
                    )

                    PerfilMenuCard(
                        titulo = "Gestión de Reservas",
                        subtitulo = "Confirmar o rechazar reservas de usuarios",
                        icon = Icons.Outlined.EventNote,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        onClick = onIrAGestionReservas
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onCerrarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Cerrar Sesión",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
