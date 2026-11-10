package com.tecsup.catalogo.data.auth

import com.tecsup.catalogo.domain.model.Sesion
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val hayUsuario: Boolean
    val correoActual: String
    val usuarioUid: StateFlow<String?>
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun registrar(correo: String, clave: String): Result<Sesion>
    suspend fun sesionActual(): Sesion?   // null si no hay sesión
    fun cerrarSesion()
}
