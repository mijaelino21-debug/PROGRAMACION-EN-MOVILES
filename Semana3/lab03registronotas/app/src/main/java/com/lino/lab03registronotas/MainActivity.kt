package com.lino.lab03registronotas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    }
}