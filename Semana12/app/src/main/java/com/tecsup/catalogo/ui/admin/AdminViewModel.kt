package com.tecsup.catalogo.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminEstadisticas(
    val totalGuardado: Int = 0,
    val totalFavoritos: Int = 0
)

class AdminViewModel(
    private val repository: HotelRepository,
    private val notificador: Notificador,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    // ---- Rol (se lee de Firestore: usuarios/{uid}.rol) ----
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    val userEmail: String
        get() = auth.currentUser?.email ?: "Usuario"

    fun cargarRol(intentos: Int = 3) {
        val uid = auth.currentUser?.uid
        Log.d("AdminVM", "cargarRol() uid=$uid intentos=$intentos")
        if (uid == null) {
            _isAdmin.value = false
            return
        }
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                Log.d("AdminVM", "existe=${doc.exists()} rol=${doc.getString("rol")}")
                _isAdmin.value = doc.getString("rol") == "admin"
            }
            .addOnFailureListener { e ->
                Log.e("AdminVM", "No se pudo leer el rol", e)
                if (intentos > 1) {
                    viewModelScope.launch {
                        delay(1500)
                        cargarRol(intentos - 1)
                    }
                } else {
                    _isAdmin.value = false
                }
            }
    }

    fun limpiarRol() {
        _isAdmin.value = false
    }

    // ---- Panel Admin ----
    private val _estadisticas = MutableStateFlow(AdminEstadisticas())
    val estadisticas: StateFlow<AdminEstadisticas> = _estadisticas.asStateFlow()

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargarEstadisticas() {
        viewModelScope.launch {
            val (total, favoritos) = repository.obtenerEstadisticas()
            _estadisticas.value = AdminEstadisticas(total, favoritos)
        }
    }

    fun sincronizar() {
        if (_sincronizando.value) return
        viewModelScope.launch {
            _sincronizando.value = true
            repository.refrescarHoteles()
                .onSuccess { cantidad ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $cantidad elementos")
                    _mensaje.value = "Sincronizado: $cantidad elementos"
                }
                .onFailure { error ->
                    _mensaje.value = mensajeDe(error)
                }
            _sincronizando.value = false
            cargarEstadisticas()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repository.limpiarCache()
            _mensaje.value = "Caché limpiada"
            cargarEstadisticas()
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}