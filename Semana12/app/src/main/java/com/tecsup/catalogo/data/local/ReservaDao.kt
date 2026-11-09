package com.tecsup.catalogo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {
    @Insert
    suspend fun insertar(reserva: ReservaEntity)

    @Query("SELECT * FROM reservas ORDER BY fechaCreacion DESC")
    fun obtenerTodas(): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun obtenerPorUsuario(usuarioId: String): Flow<List<ReservaEntity>>

    @Query("UPDATE reservas SET estado = :nuevoEstado, motivoRechazo = :motivo WHERE id = :reservaId")
    suspend fun actualizarEstado(reservaId: Long, nuevoEstado: String, motivo: String)

    @Query("SELECT COUNT(*) FROM reservas")
    suspend fun contarTotal(): Int

    @Query("SELECT COUNT(*) FROM reservas WHERE estado = :estado")
    suspend fun contarPorEstado(estado: String): Int
}
