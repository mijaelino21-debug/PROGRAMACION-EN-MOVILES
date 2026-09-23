package com.lino.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
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
fun DetalleClaseScreen(navController: NavController, claseId: String) {
    val clase = ReservasRepository.clasesDisponibles.find { it.id == claseId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            //  Header con flecha de regreso
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detalle de clase",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Banner verde con pesa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        color = Color(0xFFCBEBDC),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = Color(0xFF00695C),
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  Título y detalles
            Text(
                text = clase?.nombre ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${clase?.horario ?: ""} - ${clase?.sala ?: ""} - ${clase?.duracion ?: "45 min"}",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = clase?.descripcion ?: "",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Cupos usando totalCupos de tu ClaseModel
            Text(
                text = "${clase?.cuposDisponibles ?: 0} de ${clase?.totalCupos ?: 0} cupos disponibles",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }

        // Botón Reservar
        Button(
            onClick = {
                clase?.let {
                    ReservasRepository.agregarReserva(it, it.horario)
                    navController.navigate(Screen.Confirmacion.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00695C),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Reservar cupo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}