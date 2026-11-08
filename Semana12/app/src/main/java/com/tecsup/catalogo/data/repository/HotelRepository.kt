package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    val items: Flow<List<Hotel>>          // sale de Room
    val favoritos: Flow<List<Hotel>>
    suspend fun refrescar(): Result<Int>  // Retrofit -> Room; devuelve cuántos guardó
    suspend fun obtenerDetalle(id: Int): Result<Hotel>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}