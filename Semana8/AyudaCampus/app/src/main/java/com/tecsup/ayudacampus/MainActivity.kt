package com.tecsup.ayudacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.ayudacampus.ui.navigation.NavAyuda
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as AyudaApp
        val factory = AyudaViewModelFactory(app.repository, PreferenciasStore(applicationContext))
        setContent {
            AyudaTheme {
                NavAyuda(factory)
            }
        }
    }
}