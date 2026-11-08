package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AdminUiState {
    object Idle : AdminUiState
    object Success : AdminUiState
    data class Error(val message: String) : AdminUiState
}

class AdminViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState: StateFlow<AdminUiState> = _uiState

    // Ahora el rol viene de Firestore (usuarios/{uid}.rol == "admin")
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    val userEmail: String
        get() = auth.currentUser?.email ?: "Usuario"

    fun cargarRol() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _isAdmin.value = false
            return
        }
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                _isAdmin.value = doc.getString("rol") == "admin"
            }
            .addOnFailureListener {
                _isAdmin.value = false
            }
    }

    fun limpiarRol() {
        _isAdmin.value = false
    }
}