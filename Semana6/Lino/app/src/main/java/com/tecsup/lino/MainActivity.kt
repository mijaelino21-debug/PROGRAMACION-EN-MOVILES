package com.tecsup.lino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.lino.navigation.AppNavegacion
import com.tecsup.lino.ui.theme.LinoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinoTheme {
                AppNavegacion()
            }
        }
    }
}