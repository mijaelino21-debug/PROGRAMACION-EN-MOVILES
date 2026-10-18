package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.iconoDeCategoria
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel

private val ICONOS = listOf("pc", "code", "wifi", "key", "printer", "tag")

@Composable
fun FormularioCategoriaScreen(vm: CategoriasViewModel, categoriaId: Int, onListo: () -> Unit) {
    val categorias by vm.categorias.collectAsState()
    val existente = categorias.find { it.id == categoriaId }   // null = nueva

    var nombre by remember(existente) { mutableStateOf(existente?.nombre ?: "") }
    var icono by remember(existente) { mutableStateOf(existente?.icono ?: "pc") }
    var activa by remember(existente) { mutableStateOf(existente?.activa ?: true) }

    LaunchedEffect(vm.listo) { if (vm.listo) onListo() }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onListo) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                if (existente == null) "Nueva categoría" else "Editar categoría",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AyudaTextField(valor = nombre, onCambio = { nombre = it }, etiqueta = "Nombre de la categoría")

            Text("Ícono", fontWeight = FontWeight.Bold)
            ICONOS.chunked(3).forEach { fila ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    fila.forEach { ic ->
                        val sel = icono == ic
                        Box(
                            Modifier
                                .weight(1f)
                                .height(56.dp)
                                .background(
                                    if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { icono = ic },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                iconoDeCategoria(ic),
                                contentDescription = ic,
                                tint = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Categoría activa", fontWeight = FontWeight.Bold)
                    Text(
                        "Los estudiantes podrán elegirla",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = activa, onCheckedChange = { activa = it })
            }

            Text("Vista previa", fontWeight = FontWeight.Bold)
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(iconoDeCategoria(icono), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(nombre.ifBlank { "Nombre" }, fontWeight = FontWeight.Bold)
                }
            }

            vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            AyudaButton(
                texto = "guardar categoría",
                onClick = {
                    vm.guardar(Categoria(id = existente?.id ?: 0, nombre = nombre, icono = icono, activa = activa))
                }
            )
            OutlinedButton(onClick = onListo, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Cancelar")
            }
            if (existente != null) {
                TextButton(onClick = { vm.eliminar(existente) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Eliminar categoría", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}