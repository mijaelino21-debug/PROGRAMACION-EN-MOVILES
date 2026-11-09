package com.tecsup.catalogo.ui.reservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.catalogo.data.repository.ReservaRepository
import com.tecsup.catalogo.domain.model.Reserva
import com.tecsup.catalogo.ui.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MisReservasViewModel(
    private val reservaRepository: ReservaRepository,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    val estado: StateFlow<UiState<List<Reserva>>> = run {
        val uid = auth.currentUser?.uid ?: ""
        reservaRepository.obtenerPorUsuario(uid)
            .map<List<Reserva>, UiState<List<Reserva>>> { UiState.Exito(it) }
            .catch { emit(UiState.Error(it.localizedMessage ?: "Error al cargar reservas")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Cargando
            )
    }
}
