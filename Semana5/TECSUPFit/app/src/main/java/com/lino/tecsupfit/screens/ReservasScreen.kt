package com.lino.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lino.tecsupfit.model.*
import com.lino.tecsupfit.repository.ReservasRepository

val VerdeOscuro = Color(0xFF00695C)
val VerdeMentaFondo = Color(0xFFE0F2F1)
val VerdeTexto = Color(0xFF004D40)
val RojoCancelar = Color(0xFFC62828)

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
                ReservaCardItem(reserva = reserva)
            }
        }
    }
}

@Composable
fun ReservaCardItem(reserva: Any) {
    // Si la clase en tu ReservaModel se llama Reserva u otro nombre, accedemos por sus propiedades
    val repoReserva = reserva as com.lino.tecsupfit.model.ReservaModel
    var estadoActual by remember { mutableStateOf(repoReserva.estado) }
    val esConfirmada = estadoActual == "Confirmada"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (estadoActual == "Cancelada") Color(0xFFFAFAFA) else Color(0xFFF2F2F2)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
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
                        text = repoReserva.nombreClase,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = repoReserva.horario,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (estadoActual) {
                            "Confirmada" -> VerdeMentaFondo
                            "Cancelada" -> Color(0xFFFFEBEE)
                            else -> Color(0xFFE0E0E0)
                        }
                    ) {
                        Text(
                            text = estadoActual,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (estadoActual) {
                                "Confirmada" -> VerdeTexto
                                "Cancelada" -> RojoCancelar
                                else -> Color.Gray
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (esConfirmada) {
                TextButton(
                    onClick = {
                        repoReserva.estado = "Cancelada"
                        estadoActual = "Cancelada"
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = RojoCancelar)
                ) {
                    Text(
                        text = "Cancelar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}