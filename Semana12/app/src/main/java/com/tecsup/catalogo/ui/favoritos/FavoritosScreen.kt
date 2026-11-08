package com.tecsup.catalogo.ui.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Hotel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onHotelClick: (Int) -> Unit = {}
) {
    val favoritos by viewModel.favoritos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos") }
            )
        }
    ) { padding ->
        if (favoritos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes hoteles guardados como favoritos aún.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoritos, key = { it.id }) { hotel ->
                    ItemFavorito(
                        hotel = hotel,
                        onClick = { onHotelClick(hotel.id) },
                        onQuitarFavorito = { viewModel.alternarFavorito(hotel.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemFavorito(
    hotel: Hotel,
    onClick: () -> Unit,
    onQuitarFavorito: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = hotel.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = hotel.ciudad, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "S/ ${hotel.precioNoche} por noche",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onQuitarFavorito) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Quitar de favoritos",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}