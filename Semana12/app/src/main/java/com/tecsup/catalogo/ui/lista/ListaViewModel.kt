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

class ListaViewModel(
    private val repository: HotelRepository = HotelRepositoryImpl()
) : ViewModel() {

    private val _hoteles = MutableStateFlow<List<Hotel>>(emptyList())
    val hoteles: StateFlow<List<Hotel>> = _hoteles.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init {
        cargarHoteles()
    }

    fun cargarHoteles() {
        viewModelScope.launch {
            _cargando.value = true
            _hoteles.value = repository.getHoteles()
            _cargando.value = false
        }
    }

    fun toggleFavorito(id: Int) {
        viewModelScope.launch {
            repository.toggleFavorito(id)
            _hoteles.value = repository.getHoteles()
        }
    }
}