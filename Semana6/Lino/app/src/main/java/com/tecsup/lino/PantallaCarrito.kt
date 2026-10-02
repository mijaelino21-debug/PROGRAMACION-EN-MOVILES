package com.tecsup.lino.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lino.Producto
import com.tecsup.lino.TarjetaProducto
import com.tecsup.lino.navigation.BarraTienda

@Composable
fun PantallaCarrito(
    listaProductos: MutableList<Producto>,
    listaFavoritos: MutableList<Producto>,
    onMenuClick: () -> Unit
) {
    var nombreInput by remember { mutableStateOf("") }
    var precioInput by remember { mutableStateOf("") }
    var cantidadInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraTienda(titulo = "Carrito de Compras", onMenuClick = onMenuClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombreInput,
                onValueChange = { nombreInput = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = precioInput,
                    onValueChange = { precioInput = it },
                    label = { Text("Precio (S/)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = cantidadInput,
                    onValueChange = { cantidadInput = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (nombreInput.isNotBlank()) {
                        val precio = precioInput.toDoubleOrNull() ?: 0.0
                        val cantidad = cantidadInput.toIntOrNull() ?: 1
                        listaProductos.add(0, Producto(nombreInput, precio, cantidad))
                        nombreInput = ""
                        precioInput = ""
                        cantidadInput = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Agregar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(listaProductos) { producto ->
                    val esFav = listaFavoritos.contains(producto)
                    TarjetaProducto(
                        producto = producto,
                        esFavoritoInicial = esFav,
                        onFavoritoChange = { esFavorito ->
                            if (esFavorito) {
                                if (!listaFavoritos.contains(producto)) listaFavoritos.add(producto)
                            } else {
                                listaFavoritos.remove(producto)
                            }
                        },
                        onEliminar = {
                            listaProductos.remove(producto)
                            listaFavoritos.remove(producto)
                        }
                    )
                }
            }
        }
    }
}