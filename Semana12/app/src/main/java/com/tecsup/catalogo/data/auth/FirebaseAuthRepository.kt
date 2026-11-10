package com.tecsup.catalogo.data.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override val hayUsuario: Boolean get() = auth.currentUser != null
    override val correoActual: String get() = auth.currentUser?.email ?: "Usuario"

    private val _usuarioUid = MutableStateFlow<String?>(auth.currentUser?.uid)
    override val usuarioUid: StateFlow<String?> = _usuarioUid.asStateFlow()

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val user = auth.signInWithEmailAndPassword(correo.trim(), clave.trim()).await().user
            ?: error("No se pudo obtener el usuario")
        _usuarioUid.value = user.uid
        Result.success(Sesion(user.uid, user.email ?: correo, leerRol(user.uid)))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(Exception(traducirError(e)))
    }

    override suspend fun registrar(correo: String, clave: String): Result<Sesion> = try {
        val user = auth.createUserWithEmailAndPassword(correo.trim(), clave.trim()).await().user
            ?: error("No se pudo obtener el usuario")
        _usuarioUid.value = user.uid
        // Todo usuario nuevo nace con rol "usuario"
        db.collection("usuarios").document(user.uid)
            .set(mapOf("rol" to "usuario", "nombre" to correo.trim().substringBefore("@")))
            .await()
        Result.success(Sesion(user.uid, user.email ?: correo, Rol.USUARIO))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(Exception(traducirError(e)))
    }

    override suspend fun sesionActual(): Sesion? {
        val user = auth.currentUser ?: return null
        _usuarioUid.value = user.uid
        return Sesion(user.uid, user.email ?: "", leerRol(user.uid))
    }

    override fun cerrarSesion() {
        auth.signOut()
        _usuarioUid.value = null
    }

    private fun traducirError(e: Throwable): String {
        val msg = e.message?.lowercase() ?: ""
        return when {
            msg.contains("email-already-in-use") || msg.contains("already in use") || msg.contains("collision") -> "Este correo ya está registrado"
            msg.contains("weak-password") || (msg.contains("password") && msg.contains("six")) || msg.contains("weak") -> "La contraseña es muy débil"
            msg.contains("invalid-credential") || msg.contains("wrong-password") || msg.contains("user-not-found") || msg.contains("invalid-email") || msg.contains("credentials") -> "Credenciales incorrectas"
            msg.contains("network") || msg.contains("connection") || msg.contains("network-request-failed") -> "Sin conexión a internet"
            else -> e.localizedMessage ?: "Error de autenticación"
        }
    }

    private suspend fun leerRol(uid: String): Rol {
        repeat(3) { intento ->
            try {
                val doc = db.collection("usuarios").document(uid).get().await()
                return if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("AuthRepo", "No se pudo leer el rol", e)
                if (intento < 2) delay(1500)
            }
        }
        return Rol.USUARIO
    }
}
