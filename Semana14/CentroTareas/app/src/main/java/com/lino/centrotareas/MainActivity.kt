package com.lino.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.lino.centrotareas.ui.PantallaTareas
import com.lino.centrotareas.ui.theme.CentroTareasTheme
import com.lino.centrotareas.viewmodel.TareasViewModel
import com.lino.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: TareasViewModel by viewModels {
        TareasViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                PantallaTareas(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
