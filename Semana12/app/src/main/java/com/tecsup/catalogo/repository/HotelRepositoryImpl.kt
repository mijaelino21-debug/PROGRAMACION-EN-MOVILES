package com.tecsup.catalogo.repository

import com.tecsup.catalogo.data.remote.MockHotelApi
import com.tecsup.catalogo.domain.model.Hotel

class HotelRepositoryImpl(
    private val api: MockHotelApi = MockHotelApi()
) : HotelRepository {

    override suspend fun getHoteles(): List<Hotel> {
        return api.getHoteles()
    }

    override suspend fun getHotelById(id: Int): Hotel? {
        return api.getHoteles().find { it.id == id }
    }

    override suspend fun toggleFavorito(id: Int) {
        api.toggleFavorito(id)
    }
}