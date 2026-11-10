package com.tecsup.catalogo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND hotelId = :hotelId")
    suspend fun eliminar(usuarioId: String, hotelId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE usuarioId = :usuarioId AND hotelId = :hotelId)")
    suspend fun existe(usuarioId: String, hotelId: Int): Boolean

    @Query("SELECT hotelId FROM favoritos WHERE usuarioId = :usuarioId")
    fun hotelIdsFavoritos(usuarioId: String): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM favoritos")
    suspend fun contarFavoritos(): Int

    @Query("DELETE FROM favoritos")
    suspend fun borrarTodo()
}
