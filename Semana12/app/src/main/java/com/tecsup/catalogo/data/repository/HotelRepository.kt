package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    val items: Flow<List<Hotel>>          // Todos (para administración)
    val hotelesActivos: Flow<List<Hotel>> // Solo activos (para usuarios normales)
    val favoritos: Flow<List<Hotel>>
    suspend fun refrescar(): Result<Int>
    suspend fun obtenerDetalle(id: Int): Result<Hotel>
    suspend fun alternarFavorito(id: Int)
    suspend fun agregarHotel(hotel: Hotel): Result<Unit>
    suspend fun actualizarHotel(hotel: Hotel): Result<Unit>
    suspend fun eliminarHotel(id: Int): Result<Unit>
    suspend fun cambiarEstadoHotel(id: Int, activo: Boolean): Result<Unit>
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
