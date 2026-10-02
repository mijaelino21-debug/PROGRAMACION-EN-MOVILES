package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarPedido: () -> Unit
) {
    var nombre by remember { mutableStateOf("Mijael Lino") }
    var telefono by remember { mutableStateOf("997 281 487") }
    var direccion by remember { mutableStateOf("Av. La Molina 123") }
    var referencia by remember { mutableStateOf("Frente al parque") }
    var metodoPago by remember { mutableStateOf("Efectivo") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de entrega", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = referencia,
                onValueChange = { referencia = it },
                label = { Text("Referencia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text("Método de pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            listOf("Efectivo al entregar", "Yape", "Plin").forEach { opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (metodoPago == opcion),
                        onClick = { metodoPago = opcion }
                    )
                    Text(text = opcion, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            BotonPrimario(
                texto = "Confirmar pedido",
                onClick = onConfirmarPedido
            )
        }
    }
}