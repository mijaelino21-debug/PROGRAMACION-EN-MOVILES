package com.lino.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lino.campusgps.model.LugarConDistancia
import com.lino.campusgps.util.formatearDistancia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            val sel = item.lugar.id == seleccionado
            Card(
                onClick = { onClick(item.lugar.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (sel) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (sel) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(10.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.lugar.nombre, fontWeight = FontWeight.Bold)
                        Text(
                            item.lugar.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}