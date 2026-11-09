package com.tecsup.catalogo.ui.lista.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.ui.lista.PriceFilter
import com.tecsup.catalogo.ui.lista.RatingFilter

@Composable
fun FiltroCiudad(
    selectedCity: String?,
    availableCities: List<String>,
    onCitySelected: (String?) -> Unit
) {
    if (availableCities.isEmpty()) return

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
                    onClick = { onCitySelected(null) },
                    label = { Text("Todas") }
                )
            }
            items(availableCities) { city ->
                FilterChip(
                    selected = selectedCity == city,
                    onClick = { onCitySelected(city) },
                    label = { Text(city) }
                )
            }
        }
    }
}

@Composable
fun FiltroCalificacion(
    selectedRating: RatingFilter,
    onRatingSelected: (RatingFilter) -> Unit
) {
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
                    onClick = { onRatingSelected(RatingFilter.ALL) },
                    label = { Text("Todas") }
                )
            }
            item {
                FilterChip(
                    selected = selectedRating == RatingFilter.THREE_PLUS,
                    onClick = { onRatingSelected(RatingFilter.THREE_PLUS) },
                    label = { Text("⭐ 3+") }
                )
            }
            item {
                FilterChip(
                    selected = selectedRating == RatingFilter.FOUR_PLUS,
                    onClick = { onRatingSelected(RatingFilter.FOUR_PLUS) },
                    label = { Text("⭐ 4+") }
                )
            }
            item {
                FilterChip(
                    selected = selectedRating == RatingFilter.FIVE,
                    onClick = { onRatingSelected(RatingFilter.FIVE) },
                    label = { Text("⭐ 5") }
                )
            }
        }
    }
}

@Composable
fun FiltroPrecio(
    selectedPrice: PriceFilter,
    onPriceSelected: (PriceFilter) -> Unit
) {
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
                    onClick = { onPriceSelected(PriceFilter.ALL) },
                    label = { Text("Todos") }
                )
            }
            item {
                FilterChip(
                    selected = selectedPrice == PriceFilter.ECONOMIC,
                    onClick = { onPriceSelected(PriceFilter.ECONOMIC) },
                    label = { Text("Económico") }
                )
            }
            item {
                FilterChip(
                    selected = selectedPrice == PriceFilter.INTERMEDIATE,
                    onClick = { onPriceSelected(PriceFilter.INTERMEDIATE) },
                    label = { Text("Intermedio") }
                )
            }
            item {
                FilterChip(
                    selected = selectedPrice == PriceFilter.PREMIUM,
                    onClick = { onPriceSelected(PriceFilter.PREMIUM) },
                    label = { Text("Premium") }
                )
            }
        }
    }
}
