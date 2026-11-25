package com.lino.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lino.murocampus.ui.theme.AzulPrimario
import com.lino.murocampus.ui.theme.BordeClaro
import com.lino.murocampus.ui.theme.FondoPost
import com.lino.murocampus.viewmodel.PostsViewModel
import com.lino.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    modifier: Modifier = Modifier,
    onVolverLista: () -> Unit = {}
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    var confirmando by remember { mutableStateOf(false) }

    LaunchedEffect(borrado) {
        if (borrado is UiState.Exito) {
            vm.limpiarBorrado()
            onVolverLista()
        }
    }

    Box(modifier = Modifier.fillMaxSize().then(modifier)) {
        when (val estadoActual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(
                mensaje = estadoActual.mensaje,
                onReintentar = { vm.abrir(id) }
            )
            is UiState.Exito -> {
                val datos = estadoActual.datos
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                border = BorderStroke(1.dp, BordeClaro)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = datos.post.title,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = datos.post.body,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                        item {
                            Text(
                                text = "Comentarios (${datos.comentarios.size})",
                                color = AzulPrimario,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(items = datos.comentarios, key = { it.id }) { comentario ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(FondoPost, RoundedCornerShape(10.dp))
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = comentario.email,
                                    color = AzulPrimario,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = comentario.body,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { confirmando = true },
                            enabled = borrado !is UiState.Cargando,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Text("Eliminar")
                        }
                    }
                    if (borrado is UiState.Error) {
                        Text(
                            text = (borrado as UiState.Error).mensaje,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        if (confirmando) {
            AlertDialog(
                onDismissRequest = { confirmando = false },
                title = { Text("Eliminar publicación") },
                text = { Text("¿Seguro que quieres eliminar esta publicación?") },
                confirmButton = {
                    TextButton(onClick = {
                        confirmando = false
                        vm.borrar(id)
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { confirmando = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
