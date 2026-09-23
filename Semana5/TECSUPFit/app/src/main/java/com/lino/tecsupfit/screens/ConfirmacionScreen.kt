package com.lino.tecsupfit.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lino.tecsupfit.navigation.Screen
import com.lino.tecsupfit.repository.ReservasRepository

@Composable
fun ConfirmacionScreen(navController: NavController, claseId: String) {
    val clase = ReservasRepository.clasesDisponibles.find { it.id == claseId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //  Círculo verde menta con ícono Check verde
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFE0F2F1), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color(0xFF00695C),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Textos
        Text(
            text = "¡Cupo reservado!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = clase?.nombre ?: "",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Hoy, ${clase?.horario ?: ""} · ${clase?.sala ?: ""}",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Botón Gris Claro
        Button(
            onClick = {
                navController.navigate(Screen.Reservas.route) {
                    popUpTo(Screen.Inicio.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE8E8E8),
                contentColor = Color.DarkGray
            )
        ) {
            Text("Ver mis reservas", fontWeight = FontWeight.SemiBold)
        }
    }
}