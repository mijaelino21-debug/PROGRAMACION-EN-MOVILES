package com.tecsup.catalogo.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repository: HotelRepository
) : ViewModel() {

    // Escucha automáticamente los cambios de Room en tiempo real
    val favoritos: StateFlow<List<Hotel>> = repository.obtenerFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
        }
    }
}