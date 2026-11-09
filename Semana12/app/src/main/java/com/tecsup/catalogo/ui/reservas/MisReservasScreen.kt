package com.tecsup.catalogo.ui.reservas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.reservas.components.DetalleReservaDialog
import com.tecsup.catalogo.ui.reservas.components.ItemReservaCard
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisReservasScreen(
    viewModel: MisReservasViewModel,
    onVolver: () -> Unit,
    onExplorarHoteles: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Todas", "Próximas", "Confirmadas", "Canceladas")

    var reservaSeleccionadaParaDetalle by remember { mutableStateOf<Reserva?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Viajes", fontWeight = FontWeight.Bold) },
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
            when (val e = estado) {
                is UiState.Exito -> {
                    val total = e.datos.size
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Historial de reservas",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Administra y consulta tus estancias",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text(
                                    text = "$total",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                else -> {}
            }

            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, softWrap = false) }
                    )
                }
            }

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
                        val reservasFiltradas = when (selectedTab) {
                            1 -> reservas.filter { it.estado == "PENDIENTE" }
                            2 -> reservas.filter { it.estado == "CONFIRMADA" }
                            3 -> reservas.filter { it.estado == "CANCELADA" }
                            else -> reservas
                        }

                        if (reservasFiltradas.isEmpty()) {
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
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(24.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.EventNote,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No hay reservas en esta categoría",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Explora el catálogo de hoteles y planifica tu próxima escapada.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Button(
                                        onClick = onExplorarHoteles,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Explorar hoteles")
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(reservasFiltradas, key = { it.id }) { reserva ->
                                    ItemReservaCard(
                                        reserva = reserva,
                                        onVerDetalle = { reservaSeleccionadaParaDetalle = reserva }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (reservaSeleccionadaParaDetalle != null) {
        DetalleReservaDialog(
            reserva = reservaSeleccionadaParaDetalle!!,
            onDismiss = { reservaSeleccionadaParaDetalle = null }
        )
    }
}

@Composable
fun BadgeEstado(estado: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = when (estado) {
            "CONFIRMADA" -> androidx.compose.ui.graphics.Color(0xFF36A6C5)
            "CANCELADA" -> androidx.compose.ui.graphics.Color(0xFFE76F8A)
            else -> androidx.compose.ui.graphics.Color(0xFFFF9F43)
        }
    ) {
        Text(
            text = estado,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}
