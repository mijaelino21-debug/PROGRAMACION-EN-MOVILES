package com.tecsup.catalogo.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.admin.components.AdminReservaItemCard
import com.tecsup.catalogo.ui.admin.components.DetalleGestionReservaDialog
import com.tecsup.catalogo.ui.admin.components.TarjetaEstadisticaAdmin
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReservasScreen(
    viewModel: AdminReservasViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val estadisticas by viewModel.estadisticas.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf("Todas", "Pendientes", "Confirmadas", "Rechazadas")

    var reservaSeleccionada by remember { mutableStateOf<Reserva?>(null) }
    var mostrarDialogoConfirmar by remember { mutableStateOf<Reserva?>(null) }
    var mostrarDialogoRechazar by remember { mutableStateOf<Reserva?>(null) }
    var motivoRechazoInput by remember { mutableStateOf("") }

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
                title = { Text("Gestión de Reservas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
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
        ) {
            // Tarjetas estadísticas (2x2 grid)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TarjetaEstadisticaAdmin("Total", estadisticas.total.toString(), MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    TarjetaEstadisticaAdmin("Pendientes", estadisticas.pendientes.toString(), Color(0xFFFF9F43), Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TarjetaEstadisticaAdmin("Confirmadas", estadisticas.confirmadas.toString(), Color(0xFF36A6C5), Modifier.weight(1f))
                    TarjetaEstadisticaAdmin("Rechazadas", estadisticas.rechazadas.toString(), Color(0xFFE76F8A), Modifier.weight(1f))
                }
            }

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                placeholder = { Text("Buscar por hotel, ciudad o #ID...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtros con PrimaryTabRow
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { viewModel.onTabSelected(index) },
                        text = {
                            Text(
                                title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }
            }

            // Ordenamiento por chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ordenar:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                FilterChip(
                    selected = sortOrder == OrdenReserva.FECHA_SOLICITUD,
                    onClick = { viewModel.onSortOrderChanged(OrdenReserva.FECHA_SOLICITUD) },
                    label = { Text("Solicitud") }
                )
                FilterChip(
                    selected = sortOrder == OrdenReserva.FECHA_ENTRADA,
                    onClick = { viewModel.onSortOrderChanged(OrdenReserva.FECHA_ENTRADA) },
                    label = { Text("Entrada") }
                )
            }

            // Lista de reservas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (val e = estado) {
                    is UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = {})
                    is UiState.Exito -> {
                        val reservas = e.datos
                        if (reservas.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.EventNote,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No se encontraron reservas",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(reservas, key = { it.id }) { reserva ->
                                    AdminReservaItemCard(
                                        reserva = reserva,
                                        onClick = { reservaSeleccionada = reserva }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (reservaSeleccionada != null) {
        DetalleGestionReservaDialog(
            reserva = reservaSeleccionada!!,
            onDismiss = { reservaSeleccionada = null },
            onConfirmarClick = {
                mostrarDialogoConfirmar = reservaSeleccionada
                reservaSeleccionada = null
            },
            onRechazarClick = {
                motivoRechazoInput = ""
                mostrarDialogoRechazar = reservaSeleccionada
                reservaSeleccionada = null
            }
        )
    }

    if (mostrarDialogoConfirmar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmar = null },
            title = { Text("Confirmar Reserva", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas confirmar la reserva #${mostrarDialogoConfirmar!!.id} para el hotel ${mostrarDialogoConfirmar!!.nombreHotel}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.confirmarReserva(mostrarDialogoConfirmar!!.id)
                        mostrarDialogoConfirmar = null
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Sí, confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoConfirmar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarDialogoRechazar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoRechazar = null },
            title = { Text("Rechazar Reserva", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Estás a punto de rechazar la reserva #${mostrarDialogoRechazar!!.id}. Por favor, ingresa el motivo del rechazo:")
                    OutlinedTextField(
                        value = motivoRechazoInput,
                        onValueChange = { motivoRechazoInput = it },
                        label = { Text("Motivo de rechazo") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rechazarReserva(mostrarDialogoRechazar!!.id, motivoRechazoInput.ifBlank { "Sin motivo especificado" })
                        mostrarDialogoRechazar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Rechazar reserva")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoRechazar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}