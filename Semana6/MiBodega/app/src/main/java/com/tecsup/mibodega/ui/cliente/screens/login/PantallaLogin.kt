package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.componentes.BotonPrimario

@Composable
fun PantallaLogin(
    onLoginExitoso: () -> Unit,
    onIrACrearCuenta: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorLogin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.ilustracion_bodega),
            contentDescription = "Logo Mi Bodega",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "¡Bienvenido a Mi Bodega!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorLogin = false
            },
            label = { Text("Correo electrónico") },
            isError = errorLogin,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorLogin = false
            },
            label = { Text("Contraseña") },
            isError = errorLogin,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (errorLogin) {
            Text(
                text = "Usuario o contraseña incorrectos",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        BotonPrimario(
            texto = "Iniciar sesión",
            onClick = {
                if (email.trim() == "admin@bodega.com" && password == "123456") {
                    errorLogin = false
                    onLoginExitoso()
                } else {
                    errorLogin = true
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onIrACrearCuenta) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}