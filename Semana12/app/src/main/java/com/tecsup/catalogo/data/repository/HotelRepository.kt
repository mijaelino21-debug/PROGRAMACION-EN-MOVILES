package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    fun obtenerHoteles(): Flow<List<Hotel>>
    fun obtenerFavoritos(): Flow<List<Hotel>>
    suspend fun refrescarHoteles(): Result<Int>
    suspend fun alternarFavorito(id: Int)
    suspend fun obtenerEstadisticas(): Pair<Int, Int>
    suspend fun limpiarCache()

    suspend fun obtenerDetalle(id: Int): Result<Hotel>

}