package com.lino.avisoscampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lino.avisoscampus.data.AvisosRepositoryMemoria
import com.lino.avisoscampus.ui.PantallaAvisos
import com.lino.avisoscampus.ui.theme.AvisosCampusTheme
import com.lino.avisoscampus.viewmodel.AvisosViewModel
import com.lino.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val factory = AvisosViewModelFactory(AvisosRepositoryMemoria())
        setContent {
            AvisosCampusTheme {
                val viewModel: AvisosViewModel = viewModel(factory = factory)
                PantallaAvisos(viewModel = viewModel)
            }
        }
    }
}
