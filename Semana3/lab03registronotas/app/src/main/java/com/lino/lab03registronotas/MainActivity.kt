package com.lino.lab03registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lino.lab03registronotas.ui.theme.Lab03registronotasTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03registronotasTheme {
                RegistroNotasApp()
            }

        }
    }
}
@Composable
fun CursoItem(
    nombre: String,
    peso: String,
    valor: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(peso, color = Color(0xFF6750A4), fontSize = 12.sp)
            }
            Surface(
                color = Color(0xFFE8DEF8),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "${valor.toInt()}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF21005D),
                    fontSize = 12.sp
                )
            }
        }
        Slider(
            value = valor,
            onValueChange = onValueChange,
            valueRange = 0f..20f,
            steps = 19
        )
    }
}

@Composable
fun RegistroNotasApp() {
    var nota1 by remember { mutableStateOf(0f) }
    var nota2 by remember { mutableStateOf(0f) }
    var nota3 by remember { mutableStateOf(0f) }
    var nota4 by remember { mutableStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    var calculado by remember { mutableStateOf(false) }
    var promPonderado by remember { mutableStateOf(0.0) }
    var promFinalStr by remember { mutableStateOf("") }
    var observacion by remember { mutableStateOf("") }
    var colorChip by remember { mutableStateOf(Color.Gray) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Registro de Notas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            CursoItem("Fundamentos de Programación", "(20%)", nota1) { nota1 = it }
            CursoItem("Programación Orientada a Objetos", "(25%)", nota2) { nota2 = it }
            CursoItem("Desarrollo de Apps Móviles", "(30%)", nota3) { nota3 = it }
            CursoItem("Base de Datos", "(25%)", nota4) { nota4 = it }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("redondear promedio final", fontSize = 14.sp)
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it }
            )
            Text("confirmar que las notas seam correctas", fontSize = 13.sp)
        }
        Button(
            onClick = {
                val p = (nota1 * 0.20f) + (nota2 * 0.25f) + (nota3 * 0.30f) + (nota4 * 0.25f)
                promPonderado = p.toDouble()

                val pFinal = if (redondear) kotlin.math.round(p) else p
                promFinalStr = if (redondear) "${pFinal.toInt()}" else String.format("%.2f", pFinal)

                when {
                    pFinal >= 17.5f -> {
                        observacion = "excelente"
                        colorChip = Color(0xFF2E7D32)
                    }
                    pFinal >= 13.0f -> {
                        observacion = "aprobadoo"
                        colorChip = Color(0xFF1565C0)
                    }
                    pFinal >= 10.5f -> {
                        observacion = "en recuperacion"
                        colorChip = Color(0xFFEF6C00)
                    }
                    else -> {
                        observacion = "desaprobado"
                        colorChip = Color(0xFFC62828)
                    }
                }
                calculado = true
            },
            enabled = confirmado,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("CALCULAR PROMEDIO")
        }
    }
}