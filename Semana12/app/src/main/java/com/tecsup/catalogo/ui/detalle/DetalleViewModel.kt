package com.tecsup.catalogo.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repository: HotelRepository,
    private val hotelId: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Hotel>>(UiState.Cargando)
    val estado: StateFlow<UiState<Hotel>> = _estado.asStateFlow()

    init { cargar() }

    fun cargar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repository.obtenerDetalle(hotelId)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun alternarFavorito() {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            repository.alternarFavorito(actual.id)
            _estado.value = UiState.Exito(actual.copy(esFavorito = !actual.esFavorito))
        }
    }
}