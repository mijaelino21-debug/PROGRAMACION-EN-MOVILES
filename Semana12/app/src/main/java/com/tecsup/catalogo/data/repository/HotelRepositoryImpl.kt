package com.tecsup.catalogo.repository

import com.tecsup.catalogo.data.dto.toDomain
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.domain.model.Hotel

class HotelRepositoryImpl : HotelRepository {

    private val apiService = RetrofitClient.apiService
    private var cachedHoteles = mutableListOf<Hotel>()

    override suspend fun getHoteles(): List<Hotel> {
        return try {
            if (cachedHoteles.isEmpty()) {
                val dtos = apiService.getHoteles()
                cachedHoteles = dtos.map { it.toDomain() }.toMutableList()
            }
            cachedHoteles
        } catch (e: Exception) {
            e.printStackTrace()
            cachedHoteles
        }
    }

    override suspend fun toggleFavorito(id: Int) {
        val index = cachedHoteles.indexOfFirst { it.id == id }
        if (index != -1) {
            val hotelActual = cachedHoteles[index]
            cachedHoteles[index] = hotelActual.copy(esFavorito = !hotelActual.esFavorito)
        }
    }
}