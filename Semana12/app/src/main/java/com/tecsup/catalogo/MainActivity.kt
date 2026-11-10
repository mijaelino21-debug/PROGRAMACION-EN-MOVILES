package com.tecsup.catalogo

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.login.AuthViewModel
import com.tecsup.catalogo.ui.lista.ListaViewModel
import com.tecsup.catalogo.ui.navigation.AppNav
import com.tecsup.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {

    private val pedirPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val container = AppContainer(applicationContext)
        val factory = AppViewModelFactory(container)

        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        val listaViewModel = ViewModelProvider(this, factory)[ListaViewModel::class.java]
        val adminViewModel = ViewModelProvider(this, factory)[AdminViewModel::class.java]

        setContent {
            CatalogoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNav(
                        container = container,
                        authViewModel = authViewModel,
                        listaViewModel = listaViewModel,
                        adminViewModel = adminViewModel,
                        factory = factory
                    )
                }
            }
        }
    }
}
