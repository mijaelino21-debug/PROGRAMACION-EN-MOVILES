package com.tecsup.catalogo.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Query("SELECT * FROM hoteles ORDER BY nombre")
    fun todos(): Flow<List<HotelEntity>>

    @Query("SELECT * FROM hoteles WHERE esFavorito = 1 ORDER BY nombre")
    fun favoritos(): Flow<List<HotelEntity>>

    @Query("SELECT id FROM hoteles WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<Int>

    @Query("SELECT COUNT(*) FROM hoteles")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM hoteles WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Upsert
    suspend fun guardarTodos(hoteles: List<HotelEntity>)

    @Query("UPDATE hoteles SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: Int)

    @Query("DELETE FROM hoteles")
    suspend fun borrarTodo()
}