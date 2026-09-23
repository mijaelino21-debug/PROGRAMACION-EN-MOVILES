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
// Imports de los modelos y pantallas del proyecto
import com.example.clinicasalud.model.Cita
import com.example.clinicasalud.model.listaMedicos
import com.example.clinicasalud.screens.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    //administra el stack de pantallas e historial de navegación.
    val navController = rememberNavController()
    //  controla si el menú lateral está abierto o cerrado.
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // permite ejecutar corrutinas para animar la apertura/cierre del drawer.
    val scope = rememberCoroutineScope()
    val misCitas = remember { mutableStateListOf<Cita>() }

    //  MENÚ LATERAL
    // Envuelve el NavHost para permitir deslizar o abrir el panel de opciones.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Muestra los datos de perfil tipo avatar con iniciales y datos del paciente.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFFE8DEF8),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "ML",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A2E83)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Mijael Lino ", fontWeight = FontWeight.Bold)
                        Text("Paciente", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))

                // OPCIONES DE NAVEGACIÓN DEL DRAWER:
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
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(Screen.Home.route) }
                )
            }
        }
    ) {
        // Mapea de  rutas string definidas en la sellada 'Screen' a su respectivo composable.
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(navController) { scope.launch { drawerState.open() } }
            }
            composable(Screen.MisCitas.route) {
                MisCitasScreen(navController, { scope.launch { drawerState.open() } }, misCitas)
            }
            composable(Screen.Historial.route) {
                HistorialScreen(onMenuClick = { scope.launch { drawerState.open() } })
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
            // Pantalla de Confirmación Recibe argumentos dinámicos y guarda la cita en la lista  'misCitas'

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

                // ejecuta este bloque solo una vez cuando se entra a esta pantalla
                // Evita que se agreguen citas duplicadas si la pantalla se redibuja
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