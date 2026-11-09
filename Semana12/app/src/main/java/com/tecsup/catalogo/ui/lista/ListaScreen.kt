package com.tecsup.catalogo.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.AvisoSinConexion
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView
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
                        onCerrarSesion = onCerrarSesion
                    )
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Aviso rojo cuando no hay internet y se muestran datos guardados
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
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = when {
                                                        hasFilters -> Icons.Outlined.Search
                                                        soloFavoritos -> Icons.Outlined.FavoriteBorder
                                                        else -> Icons.Outlined.Home
                                                    },
                                                    contentDescription = null,
                                                    modifier = Modifier.size(40.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Text(
                                                text = when {
                                                    hasFilters -> "No se encontraron hoteles"
                                                    soloFavoritos -> "Sin favoritos"
                                                    else -> "Sin hoteles disponibles"
                                                },
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = when {
                                                    hasFilters -> "Intenta con otros términos de búsqueda o filtros diferentes"
                                                    soloFavoritos -> "Todavía no has agregado hoteles a tus favoritos"
                                                    else -> "Todavía no hay hoteles disponibles"
                                                },
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )

                                            if (hasFilters) {
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Button(onClick = { viewModel.limpiarFiltros() }) {
                                                    Text("Limpiar filtros")
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Column(modifier = Modifier.fillMaxSize()) {
                                        // Filtros visibles directamente en la pantalla (solo en Explorar)
                                        if (!soloFavoritos) {
                                            // 1. Barra de búsqueda
                                            OutlinedTextField(
                                                value = searchQuery,
                                                onValueChange = viewModel::onSearchQueryChanged,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                                placeholder = { Text("Buscar hotel por nombre...") },
                                                leadingIcon = {
                                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                                                },
                                                trailingIcon = {
                                                    if (searchQuery.isNotEmpty()) {
                                                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Borrar búsqueda")
                                                        }
                                                    }
                                                },
                                                singleLine = true,
                                                shape = MaterialTheme.shapes.medium
                                            )

                                            // 2. Filtro por Ciudad
                                            if (availableCities.isNotEmpty()) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 16.dp, vertical = 2.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "Ciudad:",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.width(80.dp)
                                                    )
                                                    LazyRow(
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                        contentPadding = PaddingValues(vertical = 2.dp)
                                                    ) {
                                                        item {
                                                            FilterChip(
                                                                selected = selectedCity == null,
                                                                onClick = { viewModel.onCitySelected(null) },
                                                                label = { Text("Todas") }
                                                            )
                                                        }
                                                        items(availableCities) { city ->
                                                            FilterChip(
                                                                selected = selectedCity == city,
                                                                onClick = { viewModel.onCitySelected(city) },
                                                                label = { Text(city) }
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            // 3. Filtro por Calificación
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Calificación:",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.width(80.dp)
                                                )
                                                LazyRow(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    contentPadding = PaddingValues(vertical = 2.dp)
                                                ) {
                                                    item {
                                                        FilterChip(
                                                            selected = selectedRating == RatingFilter.ALL,
                                                            onClick = { viewModel.onRatingSelected(RatingFilter.ALL) },
                                                            label = { Text("Todas") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = selectedRating == RatingFilter.THREE_PLUS,
                                                            onClick = { viewModel.onRatingSelected(RatingFilter.THREE_PLUS) },
                                                            label = { Text("⭐ 3+") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = selectedRating == RatingFilter.FOUR_PLUS,
                                                            onClick = { viewModel.onRatingSelected(RatingFilter.FOUR_PLUS) },
                                                            label = { Text("⭐ 4+") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = selectedRating == RatingFilter.FIVE,
                                                            onClick = { viewModel.onRatingSelected(RatingFilter.FIVE) },
                                                            label = { Text("⭐ 5") }
                                                        )
                                                    }
                                                }
                                            }

                                            // 4. Filtro por Precio
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Precio:",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.width(80.dp)
                                                )
                                                LazyRow(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    contentPadding = PaddingValues(vertical = 2.dp)
                                                ) {
                                                    item {
                                                        FilterChip(
                                                            selected = selectedPrice == PriceFilter.ALL,
                                                            onClick = { viewModel.onPriceSelected(PriceFilter.ALL) },
                                                            label = { Text("Todos") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = selectedPrice == PriceFilter.ECONOMIC,
                                                            onClick = { viewModel.onPriceSelected(PriceFilter.ECONOMIC) },
                                                            label = { Text("Económico") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = selectedPrice == PriceFilter.INTERMEDIATE,
                                                            onClick = { viewModel.onPriceSelected(PriceFilter.INTERMEDIATE) },
                                                            label = { Text("Intermedio") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = selectedPrice == PriceFilter.PREMIUM,
                                                            onClick = { viewModel.onPriceSelected(PriceFilter.PREMIUM) },
                                                            label = { Text("Premium") }
                                                        )
                                                    }
                                                }
                                            }

                                            // 5. Ordenamiento
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Ordenar:",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.width(80.dp)
                                                )
                                                LazyRow(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    contentPadding = PaddingValues(vertical = 2.dp)
                                                ) {
                                                    item {
                                                        FilterChip(
                                                            selected = sortOrder == SortOrder.ORIGINAL,
                                                            onClick = { viewModel.onSortOrderChanged(SortOrder.ORIGINAL) },
                                                            label = { Text("Original") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = sortOrder == SortOrder.PRICE_LOW_TO_HIGH,
                                                            onClick = { viewModel.onSortOrderChanged(SortOrder.PRICE_LOW_TO_HIGH) },
                                                            label = { Text("Menor precio ↑") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = sortOrder == SortOrder.PRICE_HIGH_TO_LOW,
                                                            onClick = { viewModel.onSortOrderChanged(SortOrder.PRICE_HIGH_TO_LOW) },
                                                            label = { Text("Mayor precio ↓") }
                                                        )
                                                    }
                                                    item {
                                                        FilterChip(
                                                            selected = sortOrder == SortOrder.BEST_RATING,
                                                            onClick = { viewModel.onSortOrderChanged(SortOrder.BEST_RATING) },
                                                            label = { Text("Mejor calificación ⭐") }
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Lista de hoteles
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            contentPadding = PaddingValues(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            items(e.datos, key = { it.id }) { hotel ->
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
            }
        }
    }
}