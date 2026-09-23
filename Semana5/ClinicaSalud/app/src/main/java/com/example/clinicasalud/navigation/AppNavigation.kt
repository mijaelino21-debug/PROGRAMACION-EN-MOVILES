package com.example.clinicasalud.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.clinicasalud.model.Cita
import com.example.clinicasalud.model.listaMedicos
import com.example.clinicasalud.screens.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val misCitas = remember { mutableStateListOf<Cita>() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Clínica Salud+", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Filled.Home, null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(Screen.Home.route) }
                )
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    icon = { Icon(Icons.Filled.CalendarMonth, null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(Screen.MisCitas.route) }
                )
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    icon = { Icon(Icons.Filled.History, null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(Screen.Historial.route) }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(navController) { scope.launch { drawerState.open() } }
            }
            composable(Screen.MisCitas.route) {
                MisCitasScreen(navController, { scope.launch { drawerState.open() } }, misCitas)
            }
            composable(Screen.Historial.route) {
                HistorialScreen(navController) { scope.launch { drawerState.open() } }
            }
            composable(
                Screen.PerfilMedico.route,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("medicoId") ?: 1
                PerfilMedicoScreen(navController, listaMedicos.first { it.id == id })
            }
            composable(
                Screen.AgendarCita.route,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("medicoId") ?: 1
                AgendarCitaScreen(navController, listaMedicos.first { it.id == id })
            }
            composable(
                Screen.Confirmacion.route,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.IntType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { backStack ->
                val id = backStack.arguments?.getInt("medicoId") ?: 1
                val fecha = backStack.arguments?.getString("fecha") ?: ""
                val hora = backStack.arguments?.getString("hora") ?: ""
                val medico = listaMedicos.first { it.id == id }

                LaunchedEffect(Unit) {
                    if (misCitas.none { it.medico.id == id && it.fecha == fecha && it.hora == hora }) {
                        misCitas.add(Cita(medico, fecha, hora))
                    }
                }
                ConfirmacionScreen(navController, medico, fecha, hora)
            }
        }
    }
}