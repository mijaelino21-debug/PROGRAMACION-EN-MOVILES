package com.tecsup.catalogo.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Completa todos los campos")
            return
        }
        _uiState.value = AuthUiState.Loading
        auth.signInWithEmailAndPassword(email.trim(), pass.trim())
            .addOnSuccessListener {
                _uiState.value = AuthUiState.Success
            }
            .addOnFailureListener { e ->
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Error al iniciar sesión")
            }
    }

    fun registrar(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Completa todos los campos")
            return
        }
        _uiState.value = AuthUiState.Loading
        auth.createUserWithEmailAndPassword(email.trim(), pass.trim())
            .addOnSuccessListener {
                _uiState.value = AuthUiState.Success
            }
            .addOnFailureListener { e ->
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Error al registrarse")
            }
    }

    fun logout() {
        auth.signOut()
        _uiState.value = AuthUiState.Idle
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}