package com.lino.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lino.tecsupfit.repository.ReservasRepository

val VerdeOscuro = Color(0xFF00695C)
val VerdeMentaFondo = Color(0xFFE0F2F1)
val VerdeTexto = Color(0xFF004D40)

@Composable
fun ReservasScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Mis reservas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(ReservasRepository.misReservas) { reserva ->
                val esConfirmada = reserva.estado == "Confirmada"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
                ) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        // 1. Barra vertical verde para reservas confirmadas
                        if (esConfirmada) {
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .fillMaxHeight()
                                    .background(VerdeOscuro)
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = reserva.nombreClase,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = reserva.horario,
                                fontSize = 13.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2. Chip de estado sin la palabra "Estado:"
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (esConfirmada) VerdeMentaFondo else Color(0xFFE0E0E0)
                            ) {
                                Text(
                                    text = reserva.estado,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (esConfirmada) VerdeTexto else Color.Gray,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}