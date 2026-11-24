package com.lino.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lino.murocampus.viewmodel.PostsViewModel
import com.lino.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize().then(modifier)) {
        when (val estadoActual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(
                mensaje = estadoActual.mensaje,
                onReintentar = { vm.cargar() }
            )
            is UiState.Exito -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = estadoActual.datos, key = { it.id }) { post ->
                    TarjetaPost(
                        userId = post.userId,
                        titulo = post.title,
                        cuerpo = post.body,
                        onClick = { onPostClick(post.id) }
                    )
                }
            }
        }
    }
}
