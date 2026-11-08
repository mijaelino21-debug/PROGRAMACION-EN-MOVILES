package com.tecsup.catalogo.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.ui.perfil.PerfilScreen

enum class PestanaActual { EXPLORAR, FAVORITOS, PERFIL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    userEmail: String,
    isAdmin: Boolean,
    onHotelClick: (Int) -> Unit,
    onIrAlAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val hoteles by viewModel.hoteles.collectAsState()
    val soloFavoritos by viewModel.soloFavoritos.collectAsState()

    var pestanaActual by remember { mutableStateOf(PestanaActual.EXPLORAR) }

    Scaffold(
        topBar = {
            if (pestanaActual != PestanaActual.PERFIL) {
                TopAppBar(
                    title = {
                        Text(if (pestanaActual == PestanaActual.FAVORITOS) "Mis Favoritos" else "Hoteles Disponibles")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = pestanaActual == PestanaActual.EXPLORAR,
                    onClick = {
                        if (soloFavoritos) viewModel.toggleFiltroFavoritos()
                        pestanaActual = PestanaActual.EXPLORAR
                    },
                    icon = { Icon(if (pestanaActual == PestanaActual.EXPLORAR) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Explorar") }
                )
                NavigationBarItem(
                    selected = pestanaActual == PestanaActual.FAVORITOS,
                    onClick = {
                        if (!soloFavoritos) viewModel.toggleFiltroFavoritos()
                        pestanaActual = PestanaActual.FAVORITOS
                    },
                    icon = { Icon(if (pestanaActual == PestanaActual.FAVORITOS) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null) },
                    label = { Text("Favoritos") }
                )
                NavigationBarItem(
                    selected = pestanaActual == PestanaActual.PERFIL,
                    onClick = { pestanaActual = PestanaActual.PERFIL },
                    icon = { Icon(if (pestanaActual == PestanaActual.PERFIL) Icons.Filled.Person else Icons.Outlined.Person, contentDescription = null) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (pestanaActual) {
                PestanaActual.PERFIL -> {
                    PerfilScreen(
                        userEmail = userEmail,
                        isAdmin = isAdmin,
                        onIrAlAdmin = onIrAlAdmin,
                        onCerrarSesion = onCerrarSesion
                    )
                }
                else -> {
                    if (hoteles.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (soloFavoritos) "No tienes hoteles agregados a favoritos" else "Cargando hoteles...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(hoteles, key = { it.id }) { hotel ->
                                ItemHotel(
                                    hotel = hotel,
                                    onFavoritoClick = { viewModel.onFavoritoClicked(hotel.id) },
                                    onClick = { onHotelClick(hotel.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}