package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.cliente.DetalleTicketScreen
import com.tecsup.ayudacampus.ui.cliente.LoginScreen
import com.tecsup.ayudacampus.ui.cliente.MisTicketsScreen
import com.tecsup.ayudacampus.ui.cliente.NuevoTicketScreen
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.soporte.AtenderTicketScreen
import com.tecsup.ayudacampus.ui.soporte.BandejaScreen
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.tecsup.ayudacampus.ui.soporte.PanelScreen
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.AtenderViewModel
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import com.tecsup.ayudacampus.viewmodel.NuevoTicketViewModel
import com.tecsup.ayudacampus.viewmodel.PanelViewModel
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
import com.tecsup.ayudacampus.viewmodel.TicketDetalleViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.ui.cliente.PerfilScreen
import com.tecsup.ayudacampus.ui.soporte.AjustesScreen

// Barras inferiores: 3 items Estudiante, 4 items Soporte
private val itemsEstudiante = listOf(
    Triple(Rutas.MIS_TICKETS, "Tickets", Icons.AutoMirrored.Filled.List),
    Triple(Rutas.NUEVO_TICKET, "Nuevo", Icons.Default.Add),
    Triple(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)
private val itemsSoporte = listOf(
    Triple(Rutas.PANEL, "Panel", Icons.Default.BarChart),
    Triple(Rutas.BANDEJA, "Tickets", Icons.AutoMirrored.Filled.List),
    Triple(Rutas.CATEGORIAS, "Categorías", Icons.Default.Sell),
    Triple(Rutas.AJUSTES, "Ajustes", Icons.Default.Settings)
)

@Composable
fun NavAyuda(factory: AyudaViewModelFactory) {
    val navController = rememberNavController()
    val sesionVm: SesionViewModel = viewModel(factory = factory)
    val perfil by sesionVm.perfilActivo.collectAsState()
    val modoOscuro by sesionVm.modoOscuro.collectAsState()
    val recibirTickets by sesionVm.recibirTickets.collectAsState()
    val esSoporte = perfil == Rol.ADMIN.name

    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    // Va a la pantalla principal del rol y limpia la pila
    fun irAInicio(rol: String) {
        val destino = if (rol == Rol.ADMIN.name) Rutas.PANEL else Rutas.MIS_TICKETS
        navController.navigate(destino) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    fun irA(ruta: String) {
        val inicio = if (esSoporte) Rutas.PANEL else Rutas.MIS_TICKETS
        navController.navigate(ruta) {
            popUpTo(inicio) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Guarda el otro perfil y navega a su grafo
    fun cambiarPerfil() = sesionVm.cambiarPerfil { rol -> irAInicio(rol) }

    // Borra el perfil y vuelve al Login
    fun cerrarSesion() = sesionVm.cerrarSesion {
        navController.navigate(Rutas.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    val barra = when (rutaActual) {
        in itemsEstudiante.map { it.first } + Rutas.DETALLE_TICKET -> itemsEstudiante
        in itemsSoporte.map { it.first } + Rutas.ATENDER_TICKET + Rutas.FORMULARIO_CATEGORIA -> itemsSoporte
        else -> null
    }

    AyudaTheme(rol = if (esSoporte) Rol.ADMIN else Rol.CLIENTE, modoOscuro = modoOscuro) {
        Scaffold(
            bottomBar = {
                if (barra != null) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        barra.forEach { (ruta, texto, icono) ->
                            NavigationBarItem(
                                selected = rutaActual == ruta ||
                                        (ruta == Rutas.MIS_TICKETS && rutaActual == Rutas.DETALLE_TICKET) ||
                                        (ruta == Rutas.BANDEJA && rutaActual == Rutas.ATENDER_TICKET) ||
                                        (ruta == Rutas.CATEGORIAS && rutaActual == Rutas.FORMULARIO_CATEGORIA),
                                onClick = { irA(ruta) },
                                icon = { Icon(icono, contentDescription = texto) },
                                label = { Text(texto) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.SPLASH) {
                    SplashScreen(
                        onComenzar = {
                            val p = perfil
                            // Si ya hay perfil guardado, salta el login
                            if (p == Rol.CLIENTE.name || p == Rol.ADMIN.name) {
                                irAInicio(p)
                            } else {
                                navController.navigate(Rutas.LOGIN) {
                                    popUpTo(Rutas.SPLASH) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                composable(Rutas.LOGIN) {
                    LoginScreen(vm = sesionVm, onLoginExitoso = { rol -> irAInicio(rol) })
                }

                // Estudiante
                composable(Rutas.MIS_TICKETS) {
                    val vm: MisTicketsViewModel = viewModel(factory = factory)
                    MisTicketsScreen(
                        vm = vm,
                        onAbrirTicket = { id -> navController.navigate(Rutas.detalleTicket(id)) },
                        onNuevo = { irA(Rutas.NUEVO_TICKET) }
                    )
                }

                composable(Rutas.NUEVO_TICKET) {
                    // Si Soporte apago "Recibir tickets", no se puede crear
                    if (!recibirTickets) {
                        MesaCerrada()
                    } else {
                        val vm: NuevoTicketViewModel = viewModel(factory = factory)
                        NuevoTicketScreen(
                            vm = vm,
                            onVolver = { navController.popBackStack() },
                            onEnviado = { navController.popBackStack(Rutas.MIS_TICKETS, inclusive = false) }
                        )
                    }
                }

                composable(
                    Rutas.DETALLE_TICKET,
                    arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
                ) { entry ->
                    val id = entry.arguments?.getInt("ticketId") ?: 0
                    val vm: TicketDetalleViewModel = viewModel(factory = factory)
                    DetalleTicketScreen(vm = vm, ticketId = id, onVolver = { navController.popBackStack() })
                }

                // Soporte
                composable(Rutas.PANEL) {
                    val vm: PanelViewModel = viewModel(factory = factory)
                    PanelScreen(vm = vm, onVerAltas = { irA(Rutas.BANDEJA) })
                }

                composable(Rutas.BANDEJA) {
                    val vm: BandejaViewModel = viewModel(factory = factory)
                    BandejaScreen(
                        vm = vm,
                        onAbrirTicket = { id -> navController.navigate(Rutas.atenderTicket(id)) }
                    )
                }

                composable(
                    Rutas.ATENDER_TICKET,
                    arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
                ) { entry ->
                    val id = entry.arguments?.getInt("ticketId") ?: 0
                    val vm: AtenderViewModel = viewModel(factory = factory)
                    AtenderTicketScreen(vm = vm, ticketId = id, onVolver = { navController.popBackStack() })
                }

                composable(Rutas.CATEGORIAS) {
                    val vm: CategoriasViewModel = viewModel(factory = factory)
                    CategoriasScreen(
                        vm = vm,
                        onEditar = { id -> navController.navigate(Rutas.formularioCategoria(id)) },
                        onNueva = { navController.navigate(Rutas.formularioCategoria(0)) }
                    )
                }

                composable(
                    Rutas.FORMULARIO_CATEGORIA,
                    arguments = listOf(navArgument("categoriaId") { type = NavType.IntType })
                ) { entry ->
                    val id = entry.arguments?.getInt("categoriaId") ?: 0
                    val vm: CategoriasViewModel = viewModel(factory = factory)
                    FormularioCategoriaScreen(vm = vm, categoriaId = id, onListo = { navController.popBackStack() })
                }

                // Pantallas temporales
                composable(Rutas.PERFIL) {
                    val ticketsVm: MisTicketsViewModel = viewModel(factory = factory)
                    PerfilScreen(
                        sesionVm = sesionVm,
                        ticketsVm = ticketsVm,
                        onCambiarPerfil = { cambiarPerfil() },
                        onCerrarSesion = { cerrarSesion() }
                    )
                }

                composable(Rutas.AJUSTES) {
                    AjustesScreen(
                        vm = sesionVm,
                        onCambiarPerfil = { cambiarPerfil() },
                        onCerrarSesion = { cerrarSesion() }
                    )
                }
            }
        }
    }
}

@Composable
private fun MesaCerrada() {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(56.dp))
        Text("Mesa de ayuda cerrada", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Por ahora no se pueden crear tickets.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}