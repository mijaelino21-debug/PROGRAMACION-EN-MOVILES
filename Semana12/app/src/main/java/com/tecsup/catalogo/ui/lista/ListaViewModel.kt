package com.tecsup.catalogo.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class RatingFilter {
    ALL, THREE_PLUS, FOUR_PLUS, FIVE
}

enum class PriceFilter {
    ALL, ECONOMIC, INTERMEDIATE, PREMIUM
}

enum class SortOrder {
    ORIGINAL,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    BEST_RATING
}

class ListaViewModel(
    private val repository: HotelRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Hotel>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Hotel>>> = _estado.asStateFlow()

    // Aviso de "sin conexión" (null = todo bien)
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _soloFavoritos = MutableStateFlow(false)
    val soloFavoritos: StateFlow<Boolean> = _soloFavoritos.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity.asStateFlow()

    private val _selectedRating = MutableStateFlow(RatingFilter.ALL)
    val selectedRating: StateFlow<RatingFilter> = _selectedRating.asStateFlow()

    private val _selectedPrice = MutableStateFlow(PriceFilter.ALL)
    val selectedPrice: StateFlow<PriceFilter> = _selectedPrice.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.ORIGINAL)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _availableCities = MutableStateFlow<List<String>>(emptyList())
    val availableCities: StateFlow<List<String>> = _availableCities.asStateFlow()

    private var todosLosHoteles: List<Hotel> = emptyList()
    private var refrescoTerminado = false

    init {
        // La lista SIEMPRE sale de Room. Retrofit solo la actualiza.
        viewModelScope.launch {
            repository.items.collect { lista ->
                todosLosHoteles = lista
                _availableCities.value = lista.map { it.ciudad }.distinct().sorted()
                // No mostramos "vacío" antes de que termine el primer refresco
                if (lista.isNotEmpty() || refrescoTerminado) publicar()
            }
        }
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            val resultado = repository.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { cantidad ->
                    _aviso.value = null
                    if (cantidad == 0) publicar()
                    notificador.mostrar("Datos actualizados", "Se guardaron $cantidad elementos")
                }
                .onFailure { error ->
                    val totalGuardado = repository.cantidadGuardada()
                    if (totalGuardado == 0) {
                        _estado.value = UiState.Error(mensajeDe(error))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                        publicar()
                    }
                }
        }
    }

    fun reintentar() {
        _estado.value = UiState.Cargando
        refrescoTerminado = false
        refrescar()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        publicar()
    }

    fun onCitySelected(city: String?) {
        _selectedCity.value = if (_selectedCity.value == city) null else city
        publicar()
    }

    fun onRatingSelected(rating: RatingFilter) {
        _selectedRating.value = rating
        publicar()
    }

    fun onPriceSelected(price: PriceFilter) {
        _selectedPrice.value = price
        publicar()
    }

    fun onSortOrderChanged(order: SortOrder) {
        _sortOrder.value = order
        publicar()
    }

    fun limpiarFiltros() {
        _searchQuery.value = ""
        _selectedCity.value = null
        _selectedRating.value = RatingFilter.ALL
        _selectedPrice.value = PriceFilter.ALL
        _sortOrder.value = SortOrder.ORIGINAL
        publicar()
    }

    private fun publicar() {
        val query = _searchQuery.value.trim()
        val city = _selectedCity.value
        val rating = _selectedRating.value
        val price = _selectedPrice.value
        val sort = _sortOrder.value
        val soloFavs = _soloFavoritos.value

        var lista = if (soloFavs) todosLosHoteles.filter { it.esFavorito } else todosLosHoteles

        if (query.isNotEmpty()) {
            lista = lista.filter { it.nombre.contains(query, ignoreCase = true) }
        }

        if (city != null) {
            lista = lista.filter { it.ciudad.equals(city, ignoreCase = true) }
        }

        lista = when (rating) {
            RatingFilter.ALL -> lista
            RatingFilter.THREE_PLUS -> lista.filter { it.estrellas >= 3 }
            RatingFilter.FOUR_PLUS -> lista.filter { it.estrellas >= 4 }
            RatingFilter.FIVE -> lista.filter { it.estrellas == 5 }
        }

        if (price != PriceFilter.ALL && todosLosHoteles.isNotEmpty()) {
            val minPrice = todosLosHoteles.minOf { it.precioNoche }
            val maxPrice = todosLosHoteles.maxOf { it.precioNoche }
            val span = maxPrice - minPrice
            val t1 = minPrice + span / 3.0
            val t2 = minPrice + 2.0 * (span / 3.0)

            lista = when (price) {
                PriceFilter.ECONOMIC -> lista.filter { it.precioNoche <= t1 }
                PriceFilter.INTERMEDIATE -> lista.filter { it.precioNoche > t1 && it.precioNoche <= t2 }
                PriceFilter.PREMIUM -> lista.filter { it.precioNoche > t2 }
                PriceFilter.ALL -> lista
            }
        }

        lista = when (sort) {
            SortOrder.ORIGINAL -> lista
            SortOrder.PRICE_LOW_TO_HIGH -> lista.sortedBy { it.precioNoche }
            SortOrder.PRICE_HIGH_TO_LOW -> lista.sortedByDescending { it.precioNoche }
            SortOrder.BEST_RATING -> lista.sortedByDescending { it.estrellas }
        }

        _estado.value = UiState.Exito(lista)
    }

    fun toggleFiltroFavoritos() {
        _soloFavoritos.value = !_soloFavoritos.value
        if (_estado.value is UiState.Exito) publicar()
    }

    fun onFavoritoClicked(hotelId: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(hotelId)
        }
    }
}