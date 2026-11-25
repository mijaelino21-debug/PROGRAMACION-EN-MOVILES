package com.lino.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lino.avisoscampus.model.Aviso
import com.lino.avisoscampus.model.Origen
import com.lino.avisoscampus.ui.theme.Azul
import com.lino.avisoscampus.ui.theme.GrisClaro
import com.lino.avisoscampus.ui.theme.TextoSecundario

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GrisClaro)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Historial",
                color = Azul,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            val recientes = avisos.take(20)
            if (recientes.isEmpty()) {
                Text(
                    text = "Sin avisos todavía",
                    modifier = Modifier.fillMaxWidth(),
                    color = TextoSecundario,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recientes.forEach { aviso -> FilaAviso(aviso) }
                }
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5FA), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EtiquetaOrigen(aviso.origen)
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = aviso.titulo,
                color = Color(0xFF1C1B1F),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = aviso.texto,
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text = aviso.hora, color = TextoSecundario, fontSize = 12.sp)
    }
}

@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val (fondo, colorTexto) = when (origen) {
        Origen.BOTON -> Azul to Color.White
        Origen.RECORDATORIO -> Color(0xFFFFC107) to Color(0xFF3B2F00)
        Origen.MODO_AVION -> Color(0xFF6A3FA0) to Color.White
    }
    Text(
        text = origen.texto,
        color = colorTexto,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
