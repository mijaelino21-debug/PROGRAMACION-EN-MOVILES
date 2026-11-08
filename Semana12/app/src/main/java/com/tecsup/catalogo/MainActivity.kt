package com.tecsup.catalogo

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.ui.admin.AdminScreen
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.auth.AuthViewModel
import com.tecsup.catalogo.ui.auth.LoginScreen
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.detalle.DetalleViewModel
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel
import com.tecsup.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {

    // Si el usuario niega el permiso, la app funciona normal pero sin avisos
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
                    val sesionLista by authViewModel.sesionLista.collectAsState()
                    val sesionActual by authViewModel.sesion.collectAsState()
                    var verAdminDirecto by rememberSaveable { mutableStateOf(false) }
                    var hotelDetalleId by rememberSaveable { mutableStateOf<Int?>(null) }

                    val sesion = sesionActual

                    when {
                        // Comprobando si ya había una sesión guardada
                        !sesionLista -> Cargando()

                        // Sin sesión: login
                        sesion == null -> LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = { }
                        )

                        else -> {
                            val isAdmin = sesion.rol == Rol.ADMIN
                            val detalleId = hotelDetalleId

                            // El botón "atrás" cierra el detalle
                            BackHandler(enabled = detalleId != null) { hotelDetalleId = null }

                            if (detalleId != null) {
                                val detalleViewModel: DetalleViewModel = viewModel(
                                    key = "detalle_$detalleId",
                                    factory = object : ViewModelProvider.Factory {
                                        @Suppress("UNCHECKED_CAST")
                                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                            return DetalleViewModel(container.hotelRepository, detalleId) as T
                                        }
                                    }
                                )
                                DetalleScreen(
                                    viewModel = detalleViewModel,
                                    onVolver = { hotelDetalleId = null }
                                )
                            } else if (isAdmin && verAdminDirecto) {
                                AdminScreen(
                                    viewModel = adminViewModel,
                                    userEmail = sesion.correo,
                                    onVolverAlCatalogo = { verAdminDirecto = false },
                                    onCerrarSesion = {
                                        verAdminDirecto = false
                                        authViewModel.logout()
                                    }
                                )
                            } else {
                                ListaScreen(
                                    viewModel = listaViewModel,
                                    userEmail = sesion.correo,
                                    isAdmin = isAdmin,
                                    onHotelClick = { hotelDetalleId = it },
                                    onIrAlAdmin = { verAdminDirecto = true },
                                    onCerrarSesion = {
                                        hotelDetalleId = null
                                        authViewModel.logout()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
