package com.tecsup.catalogo.ui.lista.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.ui.lista.ItemHotel

@Composable
fun ListaHoteles(
    hoteles: List<Hotel>,
    onFavoritoClick: (Int) -> Unit,
    onHotelClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(hoteles, key = { it.id }) { hotel ->
            ItemHotel(
                hotel = hotel,
                onFavoritoClick = { onFavoritoClick(hotel.id) },
                onClick = { onHotelClick(hotel.id) }
            )
        }
    }
}
