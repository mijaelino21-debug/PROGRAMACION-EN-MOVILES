package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.domain.model.Hotel

// Interfaz para Retrofit
interface HotelApi {
    suspend fun getHoteles(): List<Hotel>
}

// Implementación de simulación (Mock Data)
class MockHotelApi : HotelApi {
    private val listaHoteles = mutableListOf(
        Hotel(1, "Hotel Pariwana", "Excelente ambiente mochilero en el centro", "Cusco", 85.0, 4, "https://via.placeholder.com/150"),
        Hotel(2, "JW Marriott", "Lujo frente al mar en Larcomar", "Lima", 450.0, 5, "https://via.placeholder.com/150"),
        Hotel(3, "Casa Andina Premium", "Vistas espectaculares a la plaza", "Arequipa", 210.0, 4, "https://via.placeholder.com/150")
    )

    override suspend fun getHoteles(): List<Hotel> {
        return listaHoteles
    }

    fun toggleFavorito(id: Int) {
        val index = listaHoteles.indexOfFirst { it.id == id }
        if (index != -1) {
            val hotelActual = listaHoteles[index]
            listaHoteles[index] = hotelActual.copy(esFavorito = !hotelActual.esFavorito)
        }
    }
}