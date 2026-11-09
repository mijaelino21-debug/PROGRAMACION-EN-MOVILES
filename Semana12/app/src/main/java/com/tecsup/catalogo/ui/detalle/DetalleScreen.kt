package com.tecsup.catalogo.ui.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.detalle.components.ContenidoDetalle
import com.tecsup.catalogo.ui.detalle.components.DialogoReserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    isAdmin: Boolean,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val reservaEstado by viewModel.reservaEstado.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoReserva by remember { mutableStateOf(false) }

    LaunchedEffect(reservaEstado) {
        when (reservaEstado) {
            is UiState.Exito -> {
                snackbarHostState.showSnackbar("¡Reserva realizada con éxito!")
                mostrarDialogoReserva = false
                viewModel.limpiarReservaEstado()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar((reservaEstado as UiState.Error).mensaje)
                viewModel.limpiarReservaEstado()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detalle del hotel") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val e = estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = viewModel::cargar)
                is UiState.Exito -> ContenidoDetalle(
                    hotel = e.datos,
                    isAdmin = isAdmin,
                    onFavoritoClick = viewModel::alternarFavorito,
                    onReservarClick = { mostrarDialogoReserva = true }
                )
            }
        }
    }

    if (mostrarDialogoReserva && !isAdmin) {
        val hotelExito = (estado as? UiState.Exito)?.datos
        if (hotelExito != null) {
            DialogoReserva(
                hotel = hotelExito,
                reservaEstado = reservaEstado,
                onDismiss = { mostrarDialogoReserva = false },
                onConfirmar = { entrada, salida, huespedes, noches, total ->
                    viewModel.crearReserva(entrada, salida, huespedes, noches, total)
                }
            )
        }
    }
}
