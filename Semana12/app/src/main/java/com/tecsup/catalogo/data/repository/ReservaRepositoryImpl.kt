package com.tecsup.catalogo.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.catalogo.data.local.ReservaDao
import com.tecsup.catalogo.data.mapper.toDomain
import com.tecsup.catalogo.data.mapper.toEntity
import com.tecsup.catalogo.domain.model.Reserva
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReservaRepositoryImpl(
    private val reservaDao: ReservaDao,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ReservaRepository {

    override suspend fun guardarReserva(reserva: Reserva): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.e("ReservaRepo", "Error: Usuario no autenticado al intentar guardar reserva")
                return Result.failure(Exception("Usuario no autenticado"))
            }
            val reservaConUsuario = reserva.copy(usuarioId = currentUser.uid)
            reservaDao.insertar(reservaConUsuario.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ReservaRepo", "Error al guardar reserva en SQLite: ${e.message}", e)
            Result.failure(e)
        }
    }

    override fun obtenerTodas(): Flow<List<Reserva>> {
        return reservaDao.obtenerTodas().map { list -> list.map { it.toDomain() } }
    }

    override fun obtenerPorUsuario(usuarioId: String): Flow<List<Reserva>> {
        return reservaDao.obtenerPorUsuario(usuarioId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun actualizarEstado(reservaId: Long, nuevoEstado: String, motivo: String) {
        reservaDao.actualizarEstado(reservaId, nuevoEstado, motivo)
    }

    override suspend fun contarTotal(): Int = reservaDao.contarTotal()

    override suspend fun contarPorEstado(estado: String): Int = reservaDao.contarPorEstado(estado)
}
