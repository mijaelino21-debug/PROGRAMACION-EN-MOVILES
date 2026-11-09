package com.tecsup.catalogo.ui.lista.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.ui.lista.SortOrder

@Composable
fun OrdenamientoHoteles(
    sortOrder: SortOrder,
    onSortOrderChanged: (SortOrder) -> Unit
) {
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
                    onClick = { onSortOrderChanged(SortOrder.ORIGINAL) },
                    label = { Text("Original") }
                )
            }
            item {
                FilterChip(
                    selected = sortOrder == SortOrder.PRICE_LOW_TO_HIGH,
                    onClick = { onSortOrderChanged(SortOrder.PRICE_LOW_TO_HIGH) },
                    label = { Text("Menor precio ↑") }
                )
            }
            item {
                FilterChip(
                    selected = sortOrder == SortOrder.PRICE_HIGH_TO_LOW,
                    onClick = { onSortOrderChanged(SortOrder.PRICE_HIGH_TO_LOW) },
                    label = { Text("Mayor precio ↓") }
                )
            }
            item {
                FilterChip(
                    selected = sortOrder == SortOrder.BEST_RATING,
                    onClick = { onSortOrderChanged(SortOrder.BEST_RATING) },
                    label = { Text("Mejor calificación ⭐") }
                )
            }
        }
    }
}
