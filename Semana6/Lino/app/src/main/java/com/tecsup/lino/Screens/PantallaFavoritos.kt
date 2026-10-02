package com.tecsup.lino.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lino.Producto
import com.tecsup.lino.TarjetaProducto
import com.tecsup.lino.navigation.BarraTienda

@Composable
fun PantallaFavoritos(
    listaFavoritos: MutableList<Producto>,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = { BarraTienda(titulo = "Favoritos", onMenuClick = onMenuClick) }
    ) { padding ->
        if (listaFavoritos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos en favoritos",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(listaFavoritos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        esFavoritoInicial = true,
                        onFavoritoChange = { esFavorito ->
                            if (!esFavorito) {
                                listaFavoritos.remove(producto)
                            }
                        }
                    )
                }
            }
        }
    }
}