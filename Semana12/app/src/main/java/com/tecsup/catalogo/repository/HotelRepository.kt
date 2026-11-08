package com.tecsup.catalogo.repository

import com.tecsup.catalogo.domain.model.Hotel

interface HotelRepository {
    suspend fun getHoteles(): List<Hotel>
    suspend fun getHotelById(id: Int): Hotel?
    suspend fun toggleFavorito(id: Int)
}