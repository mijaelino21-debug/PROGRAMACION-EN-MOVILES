package com.tecsup.catalogo

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.catalogo.data.local.AppDatabase
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.HotelRepositoryImpl
import com.tecsup.catalogo.notification.NotificadorAndroid
import com.tecsup.catalogo.ui.admin.AdminScreen
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.auth.AuthViewModel
import com.tecsup.catalogo.ui.auth.LoginScreen
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.detalle.DetalleViewModel
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel
import androidx.compose.runtime.saveable.rememberSaveable


class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    // Si el usuario niega el permiso, la app funciona normal pero sin avisos
    private val pedirPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val context = applicationContext
        val database = AppDatabase.obtenerBaseDatos(context)
        val apiService = RetrofitClient.apiService
        val repository = HotelRepositoryImpl(apiService, database.hotelDao())
        val notificador = NotificadorAndroid(context)

        val listaFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListaViewModel(repository, notificador) as T
            }
        }
        val listaViewModel = ViewModelProvider(this, listaFactory)[ListaViewModel::class.java]

        val adminFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(repository, notificador) as T
            }
        }
        val adminViewModel = ViewModelProvider(this, adminFactory)[AdminViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by rememberSaveable { mutableStateOf(authViewModel.isUserLoggedIn) }
                    var verAdminDirecto by rememberSaveable { mutableStateOf(false) }
                    var hotelDetalleId by rememberSaveable { mutableStateOf<Int?>(null) }

                    val isAdmin by adminViewModel.isAdmin.collectAsState()

                    LaunchedEffect(isLoggedIn) {
                        if (isLoggedIn) adminViewModel.cargarRol() else adminViewModel.limpiarRol()
                    }

                    if (isLoggedIn) {
                        val detalleId = hotelDetalleId

                        // El botón "atrás" cierra el detalle
                        BackHandler(enabled = detalleId != null) { hotelDetalleId = null }

                        if (detalleId != null) {
                            val detalleViewModel: DetalleViewModel = viewModel(
                                key = "detalle_$detalleId",
                                factory = object : ViewModelProvider.Factory {
                                    @Suppress("UNCHECKED_CAST")
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        return DetalleViewModel(repository, detalleId) as T
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
                                onVolverAlCatalogo = { verAdminDirecto = false },
                                onCerrarSesion = {
                                    authViewModel.logout()
                                    verAdminDirecto = false
                                    isLoggedIn = false
                                }
                            )
                        } else {
                            ListaScreen(
                                viewModel = listaViewModel,
                                userEmail = adminViewModel.userEmail,
                                isAdmin = isAdmin,
                                onHotelClick = { hotelDetalleId = it },
                                onIrAlAdmin = { verAdminDirecto = true },
                                onCerrarSesion = {
                                    authViewModel.logout()
                                    hotelDetalleId = null
                                    isLoggedIn = false
                                }
                            )
                        }
                    } else {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                isLoggedIn = true
                            }
                        )
                    }
                }
            }
        }
    }
}