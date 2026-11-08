package com.tecsup.catalogo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.auth.AuthRepository
import com.tecsup.catalogo.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // null = no hay sesión iniciada
    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    // false mientras se comprueba si ya había una sesión guardada
    private val _sesionLista = MutableStateFlow(false)
    val sesionLista: StateFlow<Boolean> = _sesionLista.asStateFlow()

    init {
        viewModelScope.launch {
            _sesion.value = repo.sesionActual()
            _sesionLista.value = true
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Completa todos los campos")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            repo.iniciarSesion(email.trim(), pass.trim())
                .onSuccess {
                    _sesion.value = it
                    _uiState.value = AuthUiState.Success
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Error al iniciar sesión")
                }
        }
    }

    fun registrar(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Completa todos los campos")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            repo.registrar(email.trim(), pass.trim())
                .onSuccess {
                    _sesion.value = it
                    _uiState.value = AuthUiState.Success
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Error al registrarse")
                }
        }
    }

    fun logout() {
        repo.cerrarSesion()
        _sesion.value = null
        _uiState.value = AuthUiState.Idle
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}