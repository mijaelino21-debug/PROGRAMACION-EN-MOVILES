package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.iconoDeCategoria
import com.tecsup.ayudacampus.ui.theme.color
import com.tecsup.ayudacampus.viewmodel.NuevoTicketViewModel

@Composable
fun NuevoTicketScreen(
    vm: NuevoTicketViewModel,
    onVolver: () -> Unit,
    onEnviado: () -> Unit
) {
    val categorias by vm.categorias.collectAsState()

    // Al crear el ticket, vuelve a Mis tickets
    LaunchedEffect(vm.enviado) {
        if (vm.enviado) onEnviado()
    }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Nuevo ticket", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Categoría", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categorias, key = { it.id }) { c ->
                    FilterChip(
                        selected = vm.categoriaId == c.id,
                        onClick = { vm.cambiarCategoria(c.id) },
                        label = { Text(c.nombre) },
                        leadingIcon = { Icon(iconoDeCategoria(c.icono), null, Modifier.size(18.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
            vm.errorCategoria?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(12.dp))

            AyudaTextField(
                valor = vm.titulo,
                onCambio = { vm.cambiarTitulo(it) },
                etiqueta = "Título",
                error = vm.errorTitulo
            )
            Spacer(Modifier.height(8.dp))
            AyudaTextField(
                valor = vm.ubicacion,
                onCambio = { vm.cambiarUbicacion(it) },
                etiqueta = "Ubicación",
                icono = Icons.Default.LocationOn
            )
            Spacer(Modifier.height(12.dp))

            Text("Prioridad", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Prioridad.entries.forEach { p ->
                    BotonPrioridad(p, vm.prioridad == p, Modifier.weight(1f)) { vm.cambiarPrioridad(p) }
                }
            }
            Spacer(Modifier.height(12.dp))

            AyudaTextField(
                valor = vm.descripcion,
                onCambio = { vm.cambiarDescripcion(it) },
                etiqueta = "Descripción",
                error = vm.errorDescripcion,
                unaLinea = false,
                minLineas = 4
            )
            Spacer(Modifier.height(20.dp))

            AyudaButton(texto = "Enviar ticket", onClick = { vm.enviar() })
        }
    }
}

@Composable
private fun BotonPrioridad(p: Prioridad, seleccionado: Boolean, modifier: Modifier, onClick: () -> Unit) {
    if (seleccionado) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(containerColor = p.color())
        ) { Text(p.etiqueta) }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) { Text(p.etiqueta) }
    }
}