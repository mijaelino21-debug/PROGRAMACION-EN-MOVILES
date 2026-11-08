package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.domain.model.Hotel

interface HotelApi {
    suspend fun getHoteles(): List<Hotel>
}

class MockHotelApi : HotelApi {
    private val listaHoteles = mutableListOf(
        Hotel(1, "Pariwana", "Hostel mochilero céntrico y animado", "Cusco", 80.0, 3, "https://example.com/pariwana.jpg"),
        Hotel(2, "JW Marriott", "Hotel de lujo frente al mar en Miraflores", "Lima", 450.0, 5, "https://example.com/marriott.jpg"),
        Hotel(3, "Casa Andina", "Hotel cómodo y tradicional cerca de la Plaza de Armas", "Arequipa", 180.0, 4, "https://example.com/casaandina.jpg")
    )

    override suspend fun getHoteles(): List<Hotel> {
        return listaHoteles
    }

    fun toggleFavorito(id: Int) {
        val index = listaHoteles.indexOfFirst { it.id == id }
        if (index != -1) {
            val hotel = listaHoteles[index]
            listaHoteles[index] = hotel.copy(esFavorito = !hotel.esFavorito)
        }
    }
}
