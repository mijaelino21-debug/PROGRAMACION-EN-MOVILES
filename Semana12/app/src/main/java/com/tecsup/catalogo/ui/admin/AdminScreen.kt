package com.tecsup.catalogo.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.ui.reservas.BadgeEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    userEmail: String,
    onVolverAlCatalogo: () -> Unit,
    onVerAdminReservas: () -> Unit,
    onVerAdminHoteles: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estadisticas by viewModel.estadisticas.collectAsState()
    val sincronizando by viewModel.sincronizando.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val reservasRecientes by viewModel.reservasRecientes.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.cargarEstadisticas() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.mensajeMostrado()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezado de bienvenida
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "¡Bienvenido, Administrador!",
                                style = MaterialTheme.typography.titleLarge,
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
            }

            // Tarjetas de Estadísticas (2x2)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaEstadistica(
                            titulo = "Total Hoteles",
                            valor = estadisticas.totalGuardado.toString(),
                            icon = Icons.Default.Hotel,
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaEstadistica(
                            titulo = "Total Reservas",
                            valor = estadisticas.totalReservas.toString(),
                            icon = Icons.Outlined.EventNote,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaEstadistica(
                            titulo = "Pendientes",
                            valor = estadisticas.reservasPendientes.toString(),
                            icon = Icons.Outlined.EventNote,
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaEstadistica(
                            titulo = "Confirmadas",
                            valor = estadisticas.reservasConfirmadas.toString(),
                            icon = Icons.Outlined.EventNote,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Accesos directos / Acciones
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Acciones Rápidas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = onVerAdminHoteles,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Hotel, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gestión de Hoteles (CRUD)")
                        }

                        Button(
                            onClick = onVerAdminReservas,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Outlined.EventNote, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gestionar Todas las Reservas")
                        }

                        Button(
                            onClick = viewModel::sincronizar,
                            enabled = !sincronizando,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (sincronizando) "Sincronizando..." else "Sincronizar Hoteles (API)")
                        }

                        OutlinedButton(
                            onClick = viewModel::limpiarCache,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Limpiar Caché de Hoteles")
                        }

                        OutlinedButton(
                            onClick = onVolverAlCatalogo,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ver Catálogo de Hoteles")
                        }
                    }
                }
            }

            // Sección de Reservas Recientes
            item {
                Text(
                    text = "Reservas Recientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (reservasRecientes.isEmpty()) {
                item {
                    Text(
                        text = "No hay reservas recientes registradas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(reservasRecientes, key = { it.id }) { reserva ->
                    ReservaRecienteItem(
                        reserva = reserva,
                        onCambiarEstado = { nuevoEstado ->
                            viewModel.cambiarEstadoReserva(reserva.id, nuevoEstado)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaEstadistica(titulo: String, valor: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReservaRecienteItem(reserva: Reserva, onCambiarEstado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reserva.nombreHotel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                BadgeEstado(estado = reserva.estado)
            }

            Text(
                text = "Entrada: ${reserva.fechaEntrada} | Huéspedes: ${reserva.cantidadHuespedes} | S/ ${reserva.precioTotal}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text("Cambiar estado")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("PENDIENTE") },
                            onClick = {
                                expanded = false
                                onCambiarEstado("PENDIENTE")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("CONFIRMADA") },
                            onClick = {
                                expanded = false
                                onCambiarEstado("CONFIRMADA")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("CANCELADA") },
                            onClick = {
                                expanded = false
                                onCambiarEstado("CANCELADA")
                            }
                        )
                    }
                }
            }
        }
    }
}
