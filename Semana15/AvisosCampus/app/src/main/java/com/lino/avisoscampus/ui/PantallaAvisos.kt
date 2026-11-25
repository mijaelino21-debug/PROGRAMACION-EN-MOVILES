package com.lino.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.lino.avisoscampus.model.PermisoUi
import com.lino.avisoscampus.notificacion.Notificador
import com.lino.avisoscampus.receiver.ModoAvionReceiver
import com.lino.avisoscampus.ui.theme.Azul
import com.lino.avisoscampus.ui.theme.Fondo
import com.lino.avisoscampus.ui.theme.GrisClaro
import com.lino.avisoscampus.ui.theme.TextoSecundario
import com.lino.avisoscampus.viewmodel.AvisosViewModel

@Composable
fun PantallaAvisos(viewModel: AvisosViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val permiso by viewModel.permiso.collectAsState(initial = PermisoUi.SinPedir)
    val faltan by viewModel.faltan.collectAsState(initial = null)
    val escuchando by viewModel.escuchandoAvion.collectAsState(initial = false)
    val receptor = remember {
        ModoAvionReceiver { activo -> viewModel.alCambiarModoAvion(activo) }
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        concedido -> viewModel.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(context)) {
            viewModel.permisoRespondido(true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventos.collect { aviso ->
            Notificador.mostrar(context, aviso)
        }
    }

    DisposableEffect(escuchando) {
        if (escuchando) {
            ContextCompat.registerReceiver(
                context,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { context.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

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
            Tarjeta("Permiso de notificaciones") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Estado:", color = TextoSecundario, fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    EtiquetaPermiso(permiso)
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.permisoRespondido(true)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul)
                ) {
                    Text(text = "Permitir notificaciones", color = Color.White)
                }
            }
            Tarjeta("Avisos") {
                Button(
                    onClick = { viewModel.avisarAhora() },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul)
                ) {
                    Text(text = "Mostrar aviso", color = Color.White)
                }
            }
            Tarjeta("Recordatorio") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.recordarEn() },
                        enabled = faltan == null,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Azul,
                            disabledContainerColor = Color(0xFFBDBDBD),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(text = "Recordar en 10 s", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { viewModel.cancelarRecordatorio() },
                        enabled = faltan != null,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Azul),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Azul)
                    ) {
                        Text(text = "Cancelar")
                    }
                }
                faltan?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Faltan $it s",
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                }
            }
            Tarjeta("Receptor de anuncios") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Escuchar el modo avión", fontSize = 14.sp, color = TextoSecundario)
                    Switch(
                        checked = escuchando,
                        onCheckedChange = { viewModel.cambiarEscucha(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Azul,
                            uncheckedThumbColor = Color(0xFF757575),
                            uncheckedTrackColor = Color(0xFFE0E0E0),
                            uncheckedBorderColor = Color(0xFF9E9E9E)
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Activa o desactiva el modo avión del celular",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            }
            Tarjeta("Historial")
        }
    }
}

@Composable
private fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, fondo, colorTexto) = when (permiso) {
        PermisoUi.SinPedir -> Triple("Sin pedir", Color(0xFFECECEC), TextoSecundario)
        PermisoUi.Concedido -> Triple("Permitido", Color(0xFFDDF3E4), Color(0xFF237A3C))
        PermisoUi.Denegado -> Triple("Denegado", Color(0xFFFDE7E9), Color(0xFFB3261E))
    }
    Text(
        text = texto,
        color = colorTexto,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Composable
private fun Tarjeta(titulo: String, contenido: (@Composable () -> Unit)? = null) {
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
            if (contenido != null) {
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    contenido()
                }
            }
        }
    }
}
