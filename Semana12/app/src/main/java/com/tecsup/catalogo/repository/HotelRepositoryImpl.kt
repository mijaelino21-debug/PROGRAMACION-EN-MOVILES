package com.tecsup.catalogo.repository

import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.domain.model.Hotel

class HotelRepositoryImpl : HotelRepository {

    private var hotelesCache = mutableListOf<Hotel>()

    override suspend fun getHoteles(): List<Hotel> {
        return try {
            val listaDto = RetrofitClient.apiService.obtenerHoteles()
            val hotelesMapeados = listaDto.map { dto ->
                Hotel(
                    id = dto.id,
                    nombre = dto.nombre,
                    descripcion = dto.descripcion,
                    ciudad = dto.ciudad,
                    precioNoche = dto.precioNoche,
                    estrellas = dto.estrellas,
                    imagenUrl = dto.imagenUrl,
                    esFavorito = false
                )
            }
            hotelesCache = hotelesMapeados.toMutableList()
            hotelesCache
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getHotelById(id: Int): Hotel? {
        return hotelesCache.find { it.id == id }
    }

    override suspend fun toggleFavorito(id: Int) {
        val index = hotelesCache.indexOfFirst { it.id == id }
        if (index != -1) {
            val hotelActual = hotelesCache[index]
            hotelesCache[index] = hotelActual.copy(esFavorito = !hotelActual.esFavorito)
        }
    }
}