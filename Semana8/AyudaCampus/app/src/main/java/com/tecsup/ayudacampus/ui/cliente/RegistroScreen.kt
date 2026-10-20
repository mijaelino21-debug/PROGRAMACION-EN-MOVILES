package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    vm: RegistroViewModel,
    onVolverLogin: () -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(vm.registroExitoso) {
        if (vm.registroExitoso) {
            onVolverLogin()
        }
    }

    AyudaTheme(rol = Rol.CLIENTE, modoOscuro = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Registro de Estudiante",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Crea una cuenta nueva para acceder",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    AyudaTextField(
                        valor = vm.nombre,
                        onCambio = { vm.cambiarNombre(it) },
                        etiqueta = "Nombre y Apellido",
                        icono = Icons.Default.Person
                    )
                    if (vm.errorNombre != null) {
                        Text(vm.errorNombre!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    AyudaTextField(
                        valor = vm.correo,
                        onCambio = { vm.cambiarCorreo(it) },
                        etiqueta = "Correo institucional",
                        icono = Icons.Default.Email,
                        tipoTeclado = KeyboardType.Email
                    )
                    if (vm.errorCorreo != null) {
                        Text(vm.errorCorreo!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    AyudaTextField(
                        valor = vm.clave,
                        onCambio = { vm.cambiarClave(it) },
                        etiqueta = "Contraseña",
                        icono = Icons.Default.Lock,
                        esClave = true
                    )
                    if (vm.errorClave != null) {
                        Text(vm.errorClave!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    AyudaTextField(
                        valor = vm.confirmarClave,
                        onCambio = { vm.cambiarConfirmarClave(it) },
                        etiqueta = "Confirmar contraseña",
                        icono = Icons.Default.Lock,
                        esClave = true
                    )
                    if (vm.errorConfirmar != null) {
                        Text(vm.errorConfirmar!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }

                if (vm.mensajeGeneral != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(vm.mensajeGeneral!!, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(24.dp))
                AyudaButton(
                    texto = "Registrarse",
                    onClick = { vm.registrar() }
                )

                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onVolverLogin) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
    }
}
