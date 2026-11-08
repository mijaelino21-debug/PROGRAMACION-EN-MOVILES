package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    fun obtenerHoteles(): Flow<List<Hotel>>
    fun obtenerFavoritos(): Flow<List<Hotel>>
    suspend fun refrescarHoteles()
    suspend fun alternarFavorito(id: Int)
    suspend fun obtenerEstadisticas(): Pair<Int, Int>
    suspend fun limpiarCache()
}