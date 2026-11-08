package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.data.dto.HotelDto
import com.tecsup.catalogo.data.dto.toEntity
import com.tecsup.catalogo.data.local.HotelDao
import com.tecsup.catalogo.data.local.HotelEntity
import com.tecsup.catalogo.data.remote.ApiService
import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.tecsup.catalogo.data.dto.toDomain
import kotlinx.coroutines.CancellationException

class HotelRepositoryImpl(
    private val apiService: ApiService,
    private val hotelDao: HotelDao
) : HotelRepository {

    override fun obtenerHoteles(): Flow<List<Hotel>> {
        return hotelDao.todos().map { entities ->
            entities.map { entity -> entity.toHotel() }
        }
    }
    override suspend fun obtenerDetalle(id: Int): Result<Hotel> {
        return try {
            val dto = apiService.getHotel(id)
            val esFav = id in hotelDao.idsFavoritos()
            Result.success(dto.toDomain().copy(esFavorito = esFav))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Sin internet: usamos lo guardado en Room
            val local = hotelDao.porId(id)
            if (local != null) Result.success(local.toHotel()) else Result.failure(e)
        }
    }

    override fun obtenerFavoritos(): Flow<List<Hotel>> {
        return hotelDao.favoritos().map { entities ->
            entities.map { entity -> entity.toHotel() }
        }
    }

    override suspend fun refrescarHoteles(): Result<Int> {
        return try {
            val remotos: List<HotelDto> = apiService.getHoteles()
            // Los favoritos se leen ANTES de guardar para no perderlos al sincronizar
            val idsFavs = hotelDao.idsFavoritos().toSet()

            val entidades = remotos.map { dto ->
                val idInt = dto.id?.toIntOrNull() ?: 0
                dto.toEntity(esFavorito = idInt in idsFavs)
            }
            hotelDao.guardarTodos(entidades)
            Result.success(entidades.size)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun alternarFavorito(id: Int) {
        hotelDao.alternarFavorito(id)
    }

    override suspend fun obtenerEstadisticas(): Pair<Int, Int> {
        val total = hotelDao.contar()
        val favs = hotelDao.contarFavoritos()
        return Pair(total, favs)
    }

    override suspend fun limpiarCache() {
        hotelDao.borrarTodo()
    }

    // Mapper interno para evitar conflictos de imports entre paquetes
    private fun HotelEntity.toHotel(): Hotel {
        return Hotel(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            ciudad = ciudad,
            precioNoche = precioNoche,
            estrellas = estrellas,
            imagenUrl = imagenUrl,
            esFavorito = esFavorito
        )
    }
}