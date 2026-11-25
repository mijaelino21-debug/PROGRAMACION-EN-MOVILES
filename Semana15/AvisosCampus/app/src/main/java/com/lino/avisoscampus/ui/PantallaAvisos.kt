package com.lino.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lino.avisoscampus.ui.theme.Azul
import com.lino.avisoscampus.ui.theme.Fondo
import com.lino.avisoscampus.ui.theme.GrisClaro
import com.lino.avisoscampus.viewmodel.AvisosViewModel

@Composable
fun PantallaAvisos(viewModel: AvisosViewModel, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Fondo)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Azul)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = "AvisosCampus",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Tarjeta("Permiso de notificaciones")
            Tarjeta("Avisos")
            Tarjeta("Recordatorio")
            Tarjeta("Receptor de anuncios")
            Tarjeta("Historial")
        }
    }
}

@Composable
private fun Tarjeta(titulo: String) {
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
                text = titulo,
                color = Azul,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
