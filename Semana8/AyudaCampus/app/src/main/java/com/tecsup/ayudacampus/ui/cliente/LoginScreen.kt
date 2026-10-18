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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(vm: SesionViewModel, onLoginExitoso: (String) -> Unit) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    val error by vm.error.collectAsState()
    val usuario by vm.usuarioActual.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()


    // Cuando el login sale bien, avisa para navegar
    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(it.rol) }
    }

    // El tema cambia segun la pestaña segun  cian (Estudiante) o violeta (Soporte) y tambien el modo oscuro
    AyudaTheme(rol = rol, modoOscuro = oscuro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Ingresa para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonRol("Estudiante", rol == Rol.CLIENTE, Modifier.weight(1f)) {
                    rol = Rol.CLIENTE
                    vm.limpiarError()
                }
                BotonRol("Soporte TI", rol == Rol.ADMIN, Modifier.weight(1f)) {
                    rol = Rol.ADMIN
                    vm.limpiarError()
                }
            }
            Spacer(Modifier.height(20.dp))

            AyudaTextField(
                valor = correo,
                onCambio = { correo = it },
                etiqueta = "Correo",
                icono = Icons.Default.Email,
                tipoTeclado = KeyboardType.Email
            )
            Spacer(Modifier.height(12.dp))
            AyudaTextField(
                valor = clave,
                onCambio = { clave = it },
                etiqueta = "Contraseña",
                icono = Icons.Default.Lock,
                esClave = true
            )

            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            AyudaButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rol) })
        }
    }
}


@Composable
private fun BotonRol(texto: String, seleccionado: Boolean, modifier: Modifier, onClick: () -> Unit) {
    if (seleccionado) {
        Button(onClick = onClick, modifier = modifier, shape = RoundedCornerShape(12.dp)) { Text(texto) }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier, shape = RoundedCornerShape(12.dp)) { Text(texto) }
    }
}