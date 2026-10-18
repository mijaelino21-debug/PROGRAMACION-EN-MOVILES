package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.ui.cliente.BotonCambiarPerfil
import com.tecsup.ayudacampus.ui.cliente.BotonCerrarSesion
import com.tecsup.ayudacampus.ui.cliente.FilaPreferencia
import com.tecsup.ayudacampus.ui.cliente.NotaDataStore
import com.tecsup.ayudacampus.ui.cliente.TarjetaUsuario
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(vm: SesionViewModel, onCambiarPerfil: () -> Unit, onCerrarSesion: () -> Unit) {
    val usuario by vm.usuarioActual.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()
    val notificaciones by vm.notificaciones.collectAsState()
    val recibirTickets by vm.recibirTickets.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        TarjetaUsuario(usuario?.nombre ?: "", usuario?.correo ?: "", "Perfil activo")

        BotonCambiarPerfil("Modo práctica: ir a vista Estudiante", onCambiarPerfil)

        Text("Preferencias", fontWeight = FontWeight.Bold)
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column {
                FilaPreferencia("Modo oscuro", "Cambia el tema de la app", oscuro) { vm.guardarModoOscuro(it) }
                HorizontalDivider()
                FilaPreferencia("Notificaciones", "Avisar tickets nuevos", notificaciones) { vm.guardarNotificaciones(it) }
                HorizontalDivider()
                // Si se apaga, el estudiante no puede crear tickets
                FilaPreferencia("Recibir tickets", "Abre o cierra la mesa de ayuda", recibirTickets) { vm.guardarRecibirTickets(it) }
            }
        }

        NotaDataStore()
        BotonCerrarSesion(onCerrarSesion)
    }
}