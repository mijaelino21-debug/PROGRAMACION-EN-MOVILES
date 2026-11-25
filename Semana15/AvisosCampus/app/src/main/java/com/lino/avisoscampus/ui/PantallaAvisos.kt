package com.lino.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lino.avisoscampus.model.PermisoUi
import com.lino.avisoscampus.ui.theme.Azul
import com.lino.avisoscampus.ui.theme.Fondo
import com.lino.avisoscampus.ui.theme.GrisClaro
import com.lino.avisoscampus.ui.theme.TextoSecundario
import com.lino.avisoscampus.viewmodel.AvisosViewModel

@Composable
fun PantallaAvisos(viewModel: AvisosViewModel, modifier: Modifier = Modifier) {
    val permiso by viewModel.permiso.collectAsState(initial = PermisoUi.SinPedir)
    val avisos by viewModel.avisos.collectAsState(initial = emptyList())
    val mensaje by viewModel.mensaje.collectAsState(initial = null)

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                TextoCentrado(texto = "Estado: ${textoPermiso(permiso)}")
                Spacer(Modifier.height(12.dp))
                FilaBotones(principal = "Pedir permiso", secundario = "Ver ajustes")
            }

            Tarjeta("Avisos") {
                TextoCentrado(texto = "Aquí aparecerán los avisos del campus.")
                Spacer(Modifier.height(12.dp))
                FilaBotones(principal = "Enviar aviso", secundario = "Recordatorio")
            }

            Tarjeta("Recordatorio") {
                TextoCentrado(texto = "Programa un recordatorio para no olvidar tus avisos.")
                Spacer(Modifier.height(12.dp))
                FilaBotones(principal = "Programar", secundario = "Modo avión")
            }

            Tarjeta("Receptor de anuncios") {
                TextoCentrado(texto = "Recibe los anuncios emitidos en el campus.")
                Spacer(Modifier.height(12.dp))
                FilaBotones(principal = "Activar receptor", secundario = "Detener")
            }

            Tarjeta("Historial") {
                mensaje?.let { TextoCentrado(texto = it) }
                if (avisos.isEmpty()) {
                    TextoCentrado(texto = "Aún no hay avisos registrados.")
                } else {
                    avisos.forEach { aviso ->
                        TextoCentrado(
                            texto = "${aviso.hora} · ${aviso.origen.texto} · ${aviso.titulo}: ${aviso.texto}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GrisClaro)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                color = Azul,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            contenido()
        }
    }
}

@Composable
private fun TextoCentrado(texto: String) {
    Text(
        text = texto,
        modifier = Modifier.fillMaxWidth(),
        color = TextoSecundario,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun FilaBotones(principal: String, secundario: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Azul)
        ) {
            Text(principal)
        }
        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(50),
            border = androidx.compose.foundation.BorderStroke(1.dp, Azul),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Azul)
        ) {
            Text(secundario)
        }
    }
}

private fun textoPermiso(permiso: PermisoUi): String = when (permiso) {
    PermisoUi.SinPedir -> "Sin pedir"
    PermisoUi.Concedido -> "Concedido"
    PermisoUi.Denegado -> "Denegado"
}
