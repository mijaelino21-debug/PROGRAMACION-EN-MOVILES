package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AdminUiState {
    object Idle : AdminUiState
    object Success : AdminUiState
    data class Error(val message: String) : AdminUiState
}

class AdminViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState: StateFlow<AdminUiState> = _uiState

    // Revisa si el correo contiene "admin"
    val isAdmin: Boolean
        get() = auth.currentUser?.email?.contains("admin", ignoreCase = true) == true

    val userEmail: String
        get() = auth.currentUser?.email ?: "Usuario"
}