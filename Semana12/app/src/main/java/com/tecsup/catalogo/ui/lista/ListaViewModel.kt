package com.tecsup.catalogo.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.repository.HotelRepository
import com.tecsup.catalogo.repository.HotelRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ListaUiState {
    object Loading : ListaUiState
    data class Success(val hoteles: List<Hotel>) : ListaUiState
    data class Error(val message: String) : ListaUiState
}

class ListaViewModel(
    private val repository: HotelRepository = HotelRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaUiState>(ListaUiState.Loading)
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    init {
        cargarHoteles()
    }

    fun cargarHoteles() {
        viewModelScope.launch {
            _uiState.value = ListaUiState.Loading
            try {
                val lista = repository.getHoteles()
                _uiState.value = ListaUiState.Success(lista)
            } catch (e: Exception) {
                _uiState.value = ListaUiState.Error("Error al cargar hoteles")
            }
        }
    }

    fun toggleFavorito(id: Int) {
        viewModelScope.launch {
            repository.toggleFavorito(id)
            cargarHoteles()
        }
    }
}