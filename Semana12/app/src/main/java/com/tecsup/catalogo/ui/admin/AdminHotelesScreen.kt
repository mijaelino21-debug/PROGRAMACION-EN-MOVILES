package com.tecsup.catalogo.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.admin.components.AdminHotelCard
import com.tecsup.catalogo.ui.admin.components.HotelFormDialog
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHotelesScreen(
    viewModel: AdminHotelesViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var hotelEnEdicion by remember { mutableStateOf<Hotel?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var hotelAEliminar by remember { mutableStateOf<Hotel?>(null) }

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
                title = { Text("Gestión de Hoteles", fontWeight = FontWeight.Bold) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    hotelEnEdicion = null
                    mostrarFormulario = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Hotel")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = viewModel::reintentar)
                is UiState.Exito -> {
                    val hoteles = e.datos
                    if (hoteles.isEmpty()) {
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
                                    imageVector = Icons.Default.Hotel,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No hay hoteles registrados",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        hotelEnEdicion = null
                                        mostrarFormulario = true
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Agregar primer hotel")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(hoteles, key = { it.id }) { hotel ->
                                AdminHotelCard(
                                    hotel = hotel,
                                    onEditar = {
                                        hotelEnEdicion = hotel
                                        mostrarFormulario = true
                                    },
                                    onEliminar = { hotelAEliminar = hotel },
                                    onCambiarActivo = { activo ->
                                        viewModel.cambiarEstado(hotel.id, activo)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        HotelFormDialog(
            hotelInicial = hotelEnEdicion,
            onDismiss = { mostrarFormulario = false },
            onGuardar = { hotel ->
                viewModel.guardarHotel(hotel)
                mostrarFormulario = false
            }
        )
    }

    if (hotelAEliminar != null) {
        AlertDialog(
            onDismissRequest = { hotelAEliminar = null },
            title = { Text("Eliminar Hotel", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar el hotel '${hotelAEliminar!!.nombre}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarHotel(hotelAEliminar!!.id)
                        hotelAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { hotelAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
