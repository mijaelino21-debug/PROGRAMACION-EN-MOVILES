package com.lino.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lino.centrotareas.model.CargaUi
import com.lino.centrotareas.model.EstadisticaModo
import com.lino.centrotareas.model.PrimosUi
import com.lino.centrotareas.viewmodel.TareasViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("CentroTareas") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tarea con progreso",
                        color = Color.Blue,
                        style = MaterialTheme.typography.titleMedium
                    )

                    var toques by remember { mutableStateOf(0) }

                    Button(onClick = { toques++ }) {
                        Text("Tócame: $toques")
                    }

                    val corriendo by viewModel.corriendo.collectAsState()

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { viewModel.cancelar() },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }

                    val progreso by viewModel.progreso.collectAsState()

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = "${(progreso * 100).toInt()} %")

                    val mensaje by viewModel.mensaje.collectAsState()

                    Text(text = mensaje)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contar números primos",
                        color = Color.Blue,
                        style = MaterialTheme.typography.titleMedium
                    )

                    var textoHasta by remember { mutableStateOf("4200000") }
                    val primos by viewModel.primos.collectAsState()

                    OutlinedTextField(
                        value = textoHasta,
                        onValueChange = { textoHasta = it },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            onClick = {
                                val n = textoHasta.toIntOrNull()
                                if (n != null && n >= 2) viewModel.contar(n)
                            },
                            enabled = primos !is PrimosUi.Calculando
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = { viewModel.cancelarPrimos() },
                            enabled = primos is PrimosUi.Calculando
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primos) {
                        is PrimosUi.Listo -> {
                            Surface(
                                color = Color(0xFFC8E6C9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Primos hasta $textoHasta: ${estado.cantidad}")
                                    Text("Tiempo: ${estado.milisegundos} ms")
                                }
                            }
                        }
                        is PrimosUi.Cancelado -> Text("Cancelado")
                        is PrimosUi.Calculando -> {
                            Column(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { estado.progreso },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text("Calculando primos: ${(estado.progreso * 100).toInt()} %")
                            }
                        }
                        PrimosUi.Inicial -> Unit
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        color = Color.Blue,
                        style = MaterialTheme.typography.titleMedium
                    )

                    val carga by viewModel.carga.collectAsState()
                    val simularError by viewModel.simularError.collectAsState()
                    val estadisticasTiempos by viewModel.estadisticasTiempos.collectAsState()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Simular error")
                        Switch(
                            checked = simularError,
                            onCheckedChange = { viewModel.cambiarSimularError(it) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.cargarUnaPorUna() },
                            enabled = carga !is CargaUi.Cargando
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = { viewModel.cargarALaVez() },
                            enabled = carga !is CargaUi.Cargando
                        ) {
                            Text("A la vez")
                        }
                    }

                    when (val estado = carga) {
                        is CargaUi.Cargando -> CircularProgressIndicator()
                        is CargaUi.Exito -> {
                            Surface(
                                color = Color(0xFFC8E6C9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Modo: ${estado.modo}")
                                    Text("Perfil: ${estado.perfil}")
                                    Text("Pedidos: ${estado.pedidos}")
                                    Text("Tiempo: ${estado.milisegundos} ms")
                                }
                            }
                        }
                        is CargaUi.Error -> {
                            Surface(
                                color = Color(0xFFFFCDD2),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(estado.mensaje)
                                    Button(onClick = { viewModel.cargarALaVez() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                        CargaUi.Inicial -> Unit
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Comparación de tiempos",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    TablaComparacionTiempos(estadisticas = estadisticasTiempos)
                }
            }
        }
    }
}

@Composable
fun TablaComparacionTiempos(
    estadisticas: List<EstadisticaModo>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modo",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.3f)
                )
                Text(
                    text = "Cant.",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    text = "Último",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Prom.",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider()

            for (stat in estadisticas) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stat.modalidad,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1.3f)
                    )
                    Text(
                        text = stat.cantidadEjecuciones.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(0.7f)
                    )
                    Text(
                        text = if (stat.cantidadEjecuciones > 0) "${stat.ultimoTiempoMs} ms" else "-",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (stat.cantidadEjecuciones > 0) String.format(Locale.US, "%.0f ms", stat.tiempoPromedioMs) else "-",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (stat.cantidadEjecuciones > 0) "${stat.tiempoTotalMs} ms" else "-",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
