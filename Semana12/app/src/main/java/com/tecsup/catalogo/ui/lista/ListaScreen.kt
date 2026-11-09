package com.tecsup.catalogo.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.AvisoSinConexion
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.lista.components.*
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
    onVerMisReservas: () -> Unit,
    onIrAGestionHoteles: () -> Unit = {},
    onIrAGestionReservas: () -> Unit = {},
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val aviso by viewModel.aviso.collectAsState()
    val soloFavoritos by viewModel.soloFavoritos.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val selectedRating by viewModel.selectedRating.collectAsState()
    val selectedPrice by viewModel.selectedPrice.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val availableCities by viewModel.availableCities.collectAsState()

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
                        onVerMisReservas = onVerMisReservas,
                        onIrAGestionHoteles = onIrAGestionHoteles,
                        onIrAGestionReservas = onIrAGestionReservas,
                        onVerMisFavoritos = {
                            if (!soloFavoritos) viewModel.toggleFiltroFavoritos()
                            pestanaActual = PestanaActual.FAVORITOS
                        },
                        onCerrarSesion = onCerrarSesion
                    )
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AvisoSinConexion(hayConexion = aviso == null)

                        when (val e = estado) {
                            UiState.Cargando -> Cargando()

                            is UiState.Error -> ErrorView(
                                mensaje = e.mensaje,
                                onReintentar = viewModel::reintentar
                            )

                            is UiState.Exito -> {
                                val hasFilters = !soloFavoritos && (
                                        searchQuery.isNotEmpty() ||
                                                selectedCity != null ||
                                                selectedRating != RatingFilter.ALL ||
                                                selectedPrice != PriceFilter.ALL ||
                                                sortOrder != SortOrder.ORIGINAL
                                        )

                                if (e.datos.isEmpty()) {
                                    EstadoVacio(
                                        hasFilters = hasFilters,
                                        soloFavoritos = soloFavoritos,
                                        onLimpiarFiltros = viewModel::limpiarFiltros
                                    )
                                } else {
                                    Column(modifier = Modifier.fillMaxSize()) {
                                        if (!soloFavoritos) {
                                            BarraBusqueda(
                                                query = searchQuery,
                                                onQueryChanged = viewModel::onSearchQueryChanged
                                            )
                                            FiltroCiudad(
                                                selectedCity = selectedCity,
                                                availableCities = availableCities,
                                                onCitySelected = viewModel::onCitySelected
                                            )
                                            FiltroCalificacion(
                                                selectedRating = selectedRating,
                                                onRatingSelected = viewModel::onRatingSelected
                                            )
                                            FiltroPrecio(
                                                selectedPrice = selectedPrice,
                                                onPriceSelected = viewModel::onPriceSelected
                                            )
                                            OrdenamientoHoteles(
                                                sortOrder = sortOrder,
                                                onSortOrderChanged = viewModel::onSortOrderChanged
                                            )
                                        }

                                        ListaHoteles(
                                            hoteles = e.datos,
                                            onFavoritoClick = viewModel::onFavoritoClicked,
                                            onHotelClick = onHotelClick,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}