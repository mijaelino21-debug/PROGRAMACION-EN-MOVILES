package com.lino.navlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lino.navlab.navigation.AppNavigation
import com.lino.navlab.ui.theme.NavLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavLabTheme {
                AppNavigation()
            }
        }
    }
}