package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.domain.model.Reserva
import kotlinx.coroutines.flow.Flow

interface ReservaRepository {
    suspend fun guardarReserva(reserva: Reserva): Result<Unit>
    fun obtenerTodas(): Flow<List<Reserva>>
    fun obtenerPorUsuario(usuarioId: String): Flow<List<Reserva>>
    suspend fun actualizarEstado(reservaId: Long, nuevoEstado: String, motivo: String = "")
    suspend fun contarTotal(): Int
    suspend fun contarPorEstado(estado: String): Int
}
