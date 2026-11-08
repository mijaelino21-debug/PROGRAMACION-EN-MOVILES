package com.tecsup.catalogo.ui.lista

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.data.repository.HotelRepositoryImpl
import com.tecsup.catalogo.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    application: Application,
    private val repository: HotelRepositoryImpl
) : AndroidViewModel(application) {

    private val notificationHelper = NotificationHelper(application)

    private val _hoteles = MutableStateFlow<List<Hotel>>(emptyList())
    val hoteles: StateFlow<List<Hotel>> = _hoteles

    private val _soloFavoritos = MutableStateFlow(false)
    val soloFavoritos: StateFlow<Boolean> = _soloFavoritos

    private var todosLosHoteles: List<Hotel> = emptyList()

    init {
        cargarHoteles()
    }

    fun cargarHoteles() {
        viewModelScope.launch {
            try {
                // Sincroniza datos remotos con la base de datos local
                repository.refrescarHoteles()

                // Escucha el Flow continuo de la base de datos local
                repository.obtenerHoteles().collect { lista ->
                    todosLosHoteles = lista
                    aplicarFiltro(lista)

                    if (lista.isNotEmpty()) {
                        notificationHelper.mostrarNotificacionSincronizacion(lista.size)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun aplicarFiltro(lista: List<Hotel>) {
        if (_soloFavoritos.value) {
            _hoteles.value = lista.filter { it.esFavorito }
        } else {
            _hoteles.value = lista
        }
    }

    fun toggleFiltroFavoritos() {
        _soloFavoritos.value = !_soloFavoritos.value
        aplicarFiltro(todosLosHoteles)
    }

    fun onFavoritoClicked(hotelId: Int) {
        viewModelScope.launch {
            // Llama a alternarFavorito tal como está definido en tu repositorio
            repository.alternarFavorito(hotelId)
        }
    }
}