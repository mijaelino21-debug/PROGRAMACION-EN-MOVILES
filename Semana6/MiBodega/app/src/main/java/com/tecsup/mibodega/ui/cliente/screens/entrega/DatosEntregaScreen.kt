package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarPedido: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    // Validación
    var errorNombre by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }
    var errorDireccion by remember { mutableStateOf(false) }

    var metodoPago by remember { mutableStateOf("Efectivo") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Datos de entrega",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Campo Nombre
        Text("Nombre", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = false
            },
            placeholder = { Text("Juan Pérez") },
            isError = errorNombre,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        if (errorNombre) {
            Text(
                text = "El nombre es obligatorio",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Campo Teléfono
        Text("Teléfono", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                errorTelefono = false
            },
            placeholder = { Text("987 654 321") },
            isError = errorTelefono,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        if (errorTelefono) {
            Text(
                text = "El teléfono es obligatorio",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Campo Dirección
        Text("Dirección", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = direccion,
            onValueChange = {
                direccion = it
                errorDireccion = false
            },
            placeholder = { Text("Av. Los Olivos 123") },
            isError = errorDireccion,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        if (errorDireccion) {
            Text(
                text = "La dirección es obligatoria",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Campo Referencia
        Text("Referencia", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = referencia,
            onValueChange = { referencia = it },
            placeholder = { Text("Frente al parque") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(Modifier.height(20.dp))

        // Método de Pago
        Text("Método de pago", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        // Opción Efectivo
        FilaMetodoPago(
            nombre = "Efectivo al entregar",
            nombreImagenDrawable = "efectivo", // Busca la imagen en res/drawable
            seleccionado = (metodoPago == "Efectivo"),
            onClick = { metodoPago = "Efectivo" }
        )

        // Opción Yape
        FilaMetodoPago(
            nombre = "Yape",
            nombreImagenDrawable = "yape", // Busca la imagen en res/drawable
            seleccionado = (metodoPago == "Yape"),
            onClick = { metodoPago = "Yape" }
        )

        // Opción Plin
        FilaMetodoPago(
            nombre = "Plin",
            nombreImagenDrawable = "plin", // Busca la imagen en res/drawable
            seleccionado = (metodoPago == "Plin"),
            onClick = { metodoPago = "Plin" }
        )

        Spacer(Modifier.height(24.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            onClick = {
                val esNombreValido = nombre.isNotBlank()
                val esTelefonoValido = telefono.isNotBlank()
                val esDireccionValida = direccion.isNotBlank()

                errorNombre = !esNombreValido
                errorTelefono = !esTelefonoValido
                errorDireccion = !esDireccionValida

                if (esNombreValido && esTelefonoValido && esDireccionValida) {
                    onConfirmarPedido()
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FilaMetodoPago(
    nombre: String,
    nombreImagenDrawable: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val resourceId = remember(nombreImagenDrawable) {
        context.resources.getIdentifier(nombreImagenDrawable, "drawable", context.packageName)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = seleccionado,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
        )

        if (resourceId != 0) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = nombre,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(10.dp))
        }

        Text(text = nombre, fontWeight = FontWeight.Medium)
    }
}