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
import com.tecsup.catalogo.ui.admin.AdminHotelesScreen
import com.tecsup.catalogo.ui.admin.AdminHotelesViewModel
import com.tecsup.catalogo.ui.admin.AdminReservasScreen
import com.tecsup.catalogo.ui.admin.AdminReservasViewModel
import com.tecsup.catalogo.ui.admin.AdminScreen
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.auth.AuthViewModel
import com.tecsup.catalogo.ui.auth.LoginScreen
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.detalle.DetalleViewModel
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel
import com.tecsup.catalogo.ui.reservas.MisReservasScreen
import com.tecsup.catalogo.ui.reservas.MisReservasViewModel
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
                    val sesionLista by authViewModel.sesionLista.collectAsState()
                    val sesionActual by authViewModel.sesion.collectAsState()

                    var hotelDetalleId by rememberSaveable { mutableStateOf<Int?>(null) }
                    var verMisReservas by rememberSaveable { mutableStateOf(false) }
                    var verAdminReservas by rememberSaveable { mutableStateOf(false) }
                    var verAdminHoteles by rememberSaveable { mutableStateOf(false) }
                    var verCatalogoAdmin by rememberSaveable { mutableStateOf(false) }

                    val sesion = sesionActual

                    when {
                        !sesionLista -> Cargando()

                        sesion == null -> LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = { }
                        )

                        else -> {
                            val isAdmin = sesion.rol == Rol.ADMIN

                            if (!isAdmin) {
                                verAdminReservas = false
                                verAdminHoteles = false
                                verCatalogoAdmin = false
                            }

                            BackHandler(
                                enabled = hotelDetalleId != null ||
                                        verMisReservas ||
                                        verAdminReservas ||
                                        verAdminHoteles ||
                                        (isAdmin && verCatalogoAdmin)
                            ) {
                                when {
                                    hotelDetalleId != null -> hotelDetalleId = null
                                    verMisReservas -> verMisReservas = false
                                    verAdminReservas -> verAdminReservas = false
                                    verAdminHoteles -> verAdminHoteles = false
                                    isAdmin && verCatalogoAdmin -> verCatalogoAdmin = false
                                }
                            }

                            when {
                                hotelDetalleId != null -> {
                                    val detalleId = hotelDetalleId!!

                                    val detalleViewModel: DetalleViewModel = viewModel(
                                        key = "detalle_$detalleId",
                                        factory = object : ViewModelProvider.Factory {
                                            @Suppress("UNCHECKED_CAST")
                                            override fun <T : ViewModel> create(
                                                modelClass: Class<T>
                                            ): T {
                                                return DetalleViewModel(
                                                    container.hotelRepository,
                                                    container.reservaRepository,
                                                    container.authRepository,
                                                    detalleId
                                                ) as T
                                            }
                                        }
                                    )

                                    DetalleScreen(
                                        viewModel = detalleViewModel,
                                        isAdmin = isAdmin,
                                        onVolver = { hotelDetalleId = null }
                                    )
                                }

                                verMisReservas && !isAdmin -> {
                                    val vm: MisReservasViewModel =
                                        viewModel(factory = factory)

                                    MisReservasScreen(
                                        viewModel = vm,
                                        onVolver = { verMisReservas = false }
                                    )
                                }

                                isAdmin && verAdminReservas -> {
                                    val vm: AdminReservasViewModel =
                                        viewModel(factory = factory)

                                    AdminReservasScreen(
                                        viewModel = vm,
                                        onVolver = { verAdminReservas = false }
                                    )
                                }

                                isAdmin && verAdminHoteles -> {
                                    val vm: AdminHotelesViewModel =
                                        viewModel(factory = factory)

                                    AdminHotelesScreen(
                                        viewModel = vm,
                                        onVolver = { verAdminHoteles = false }
                                    )
                                }

                                isAdmin && verCatalogoAdmin -> {
                                    ListaScreen(
                                        viewModel = listaViewModel,
                                        userEmail = sesion.correo,
                                        isAdmin = true,
                                        onHotelClick = { hotelDetalleId = it },
                                        onIrAlAdmin = { verCatalogoAdmin = false },
                                        onVerMisReservas = { },
                                        onIrAGestionHoteles = { verAdminHoteles = true },
                                        onIrAGestionReservas = { verAdminReservas = true },
                                        onCerrarSesion = {
                                            hotelDetalleId = null
                                            verCatalogoAdmin = false
                                            verAdminReservas = false
                                            verAdminHoteles = false
                                            authViewModel.logout()
                                        }
                                    )
                                }

                                isAdmin -> {
                                    AdminScreen(
                                        viewModel = adminViewModel,
                                        userEmail = sesion.correo,
                                        onVolverAlCatalogo = {
                                            verCatalogoAdmin = true
                                        },
                                        onVerAdminReservas = {
                                            verAdminReservas = true
                                        },
                                        onVerAdminHoteles = {
                                            verAdminHoteles = true
                                        },
                                        onCerrarSesion = {
                                            hotelDetalleId = null
                                            verMisReservas = false
                                            verAdminReservas = false
                                            verAdminHoteles = false
                                            verCatalogoAdmin = false
                                            authViewModel.logout()
                                        }
                                    )
                                }

                                else -> {
                                    ListaScreen(
                                        viewModel = listaViewModel,
                                        userEmail = sesion.correo,
                                        isAdmin = false,
                                        onHotelClick = { hotelDetalleId = it },
                                        onIrAlAdmin = { },
                                        onCerrarSesion = {
                                            hotelDetalleId = null
                                            verMisReservas = false
                                            authViewModel.logout()
                                        },
                                        onVerMisReservas = {
                                            verMisReservas = true
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
}