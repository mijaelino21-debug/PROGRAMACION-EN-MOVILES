package com.tecsup.ayudacampus.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.Rol
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: AyudaRepository) : ViewModel() {

    var nombre by mutableStateOf(""); private set
    var correo by mutableStateOf(""); private set
    var clave by mutableStateOf(""); private set
    var confirmarClave by mutableStateOf(""); private set

    var errorNombre by mutableStateOf<String?>(null); private set
    var errorCorreo by mutableStateOf<String?>(null); private set
    var errorClave by mutableStateOf<String?>(null); private set
    var errorConfirmar by mutableStateOf<String?>(null); private set

    var registroExitoso by mutableStateOf(false); private set
    var mensajeGeneral by mutableStateOf<String?>(null); private set

    fun cambiarNombre(v: String) { nombre = v; errorNombre = null }
    fun cambiarCorreo(v: String) { correo = v; errorCorreo = null; mensajeGeneral = null }
    fun cambiarClave(v: String) { clave = v; errorClave = null }
    fun cambiarConfirmarClave(v: String) { confirmarClave = v; errorConfirmar = null }

    fun registrar() {
        var valido = true
        if (nombre.isBlank()) {
            errorNombre = "El nombre es obligatorio"
            valido = false
        }
        if (correo.isBlank()) {
            errorCorreo = "El correo es obligatorio"
            valido = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
            errorCorreo = "Formato de correo inválido"
            valido = false
        }
        if (clave.isBlank()) {
            errorClave = "La contraseña es obligatoria"
            valido = false
        } else if (clave.length < 6) {
            errorClave = "Mínimo 6 caracteres"
            valido = false
        }
        if (confirmarClave != clave) {
            errorConfirmar = "Las contraseñas no coinciden"
            valido = false
        }

        if (!valido) return

        viewModelScope.launch {
            try {
                val existente = repo.buscarPorCorreo(correo.trim())
                if (existente != null) {
                    errorCorreo = "Este correo ya está registrado"
                    return@launch
                }

                val nuevoUsuario = Usuario(
                    nombre = nombre.trim(),
                    correo = correo.trim(),
                    clave = clave,
                    rol = Rol.CLIENTE.name
                )
                repo.registrarUsuario(nuevoUsuario)
                registroExitoso = true
            } catch (e: Exception) {
                mensajeGeneral = "Error al registrar: ${e.localizedMessage}"
            }
        }
    }
}
