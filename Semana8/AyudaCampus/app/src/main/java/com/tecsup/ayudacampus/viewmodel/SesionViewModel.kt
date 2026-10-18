package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: AyudaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Preferencias guardadas en DataStore
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val recibirTickets: StateFlow<Boolean> = prefs.recibirTickets
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Perfil guardado en DataStore ("CLIENTE", "ADMIN" o "")
    val perfilActivo: StateFlow<String?> = prefs.perfilActivo
        .map<String, String?> { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    init {
        // Recupera el usuario guardado al abrir la app
        viewModelScope.launch {
            val id = prefs.usuarioId.first()
            if (id != 0) _usuarioActual.value = repo.usuarioPorId(id)
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        // Regla: campos vacios
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                // Regla: credenciales incorrectas
                usuario == null -> _error.value = "Correo o contraseña incorrectos"

                // Regla: el rol no coincide con la pestaña elegida
                usuario.rol != rolSeleccionado.name -> _error.value =
                    if (rolSeleccionado == Rol.CLIENTE) "Esta cuenta no es de Estudiante"
                    else "Esta cuenta no es de Soporte TI"

                // Exito: guarda el perfil en DataStore
                else -> {
                    _error.value = null
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    fun limpiarError() { _error.value = null }

    fun guardarModoOscuro(b: Boolean) { viewModelScope.launch { prefs.guardarModoOscuro(b) } }
    fun guardarNotificaciones(b: Boolean) { viewModelScope.launch { prefs.guardarNotificaciones(b) } }
    fun guardarRecibirTickets(b: Boolean) { viewModelScope.launch { prefs.guardarRecibirTickets(b) } }

    // Modo practica: guarda el otro perfil sin volver a loguearse
    fun cambiarPerfil(alTerminar: (String) -> Unit) {
        viewModelScope.launch {
            val otroRol = if (perfilActivo.value == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
            val otro = repo.usuarioPorRol(otroRol.name) ?: return@launch
            prefs.guardarSesion(otro.rol, otro.id)
            _usuarioActual.value = otro
            alTerminar(otro.rol)
        }
    }

    // Borra el perfil y vuelve al Login
    fun     cerrarSesion(alTerminar: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            alTerminar()
        }
    }
}