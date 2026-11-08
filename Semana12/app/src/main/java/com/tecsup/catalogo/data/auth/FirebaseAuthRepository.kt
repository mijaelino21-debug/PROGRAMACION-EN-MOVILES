package com.tecsup.catalogo.data.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override val hayUsuario: Boolean get() = auth.currentUser != null
    override val correoActual: String get() = auth.currentUser?.email ?: "Usuario"

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val user = auth.signInWithEmailAndPassword(correo.trim(), clave.trim()).await().user
            ?: error("No se pudo obtener el usuario")
        Result.success(Sesion(user.uid, user.email ?: correo, leerRol(user.uid)))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun registrar(correo: String, clave: String): Result<Sesion> = try {
        val user = auth.createUserWithEmailAndPassword(correo.trim(), clave.trim()).await().user
            ?: error("No se pudo obtener el usuario")
        // Todo usuario nuevo nace con rol "usuario"
        db.collection("usuarios").document(user.uid)
            .set(mapOf("rol" to "usuario", "nombre" to correo.trim().substringBefore("@")))
            .await()
        Result.success(Sesion(user.uid, user.email ?: correo, Rol.USUARIO))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sesionActual(): Sesion? {
        val user = auth.currentUser ?: return null
        return Sesion(user.uid, user.email ?: "", leerRol(user.uid))
    }

    override fun cerrarSesion() = auth.signOut()

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