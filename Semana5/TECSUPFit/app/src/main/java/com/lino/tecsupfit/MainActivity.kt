package com.lino.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lino.tecsupfit.navigation.AppNavigation
import com.lino.tecsupfit.ui.theme.TECSUPFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TECSUPFitTheme {
                AppNavigation()
            }
        }
    }
}