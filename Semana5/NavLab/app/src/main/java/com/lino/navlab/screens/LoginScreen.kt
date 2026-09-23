package com.lino.navlab.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lino.navlab.navigation.Screen
import com.lino.navlab.ui.theme.GradientEnd
import com.lino.navlab.ui.theme.GradientStart
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Portal Académico",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = GradientStart
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Accede a tu cuenta",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = { Text("Correo Institucional") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GradientStart) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = emailError != null,
                        supportingText = {
                            if (emailError != null) {
                                Text(
                                    text = emailError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GradientStart) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError != null,
                        supportingText = {
                            if (passwordError != null) {
                                Text(
                                    text = passwordError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            var valid = true
                            val cleanEmail = email.trim()

                            // i. Campo vacío
                            // ii. Formato de correo inválido (Patterns.EMAIL_ADDRESS)
                            // iv. Validación de correo institucional (@tecsup.edu.pe)
                            if (cleanEmail.isBlank()) {
                                emailError = "Este campo es obligatorio"
                                valid = false
                            } else if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
                                emailError = "Correo inválido"
                                valid = false
                            } else if (!cleanEmail.lowercase().endsWith("@tecsup.edu.pe")) {
                                emailError = "Correo no registrado"
                                valid = false
                            } else {
                                emailError = null
                            }

                            // i. Campo vacío
                            // iii. Contraseña < 8 caracteres
                            if (password.isBlank()) {
                                passwordError = "Este campo es obligatorio"
                                valid = false
                            } else if (password.length < 8) {
                                passwordError = "La contraseña debe tener al menos 8 caracteres"
                                valid = false
                            } else {
                                passwordError = null
                            }

                            if (valid) {
                                val userName = extractUserNameFromEmail(cleanEmail)
                                navController.navigate(Screen.Home.createRoute(userName)) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GradientStart)
                    ) {
                        Text("INICIAR SESIÓN", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = GradientStart,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            scope.launch {
                                snackbarHostState.showSnackbar("Se enviará un enlace de recuperación a tu correo institucional")
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun extractUserNameFromEmail(email: String): String {
    val prefix = email.substringBefore("@")
    val parts = prefix.split(".", "_", "-").filter { it.isNotEmpty() }
    return if (parts.isEmpty()) {
        "Estudiante"
    } else {
        parts.joinToString(" ") { part ->
            part.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
