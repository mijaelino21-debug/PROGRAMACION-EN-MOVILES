package com.tecsup.catalogo.data.repository

import android.util.Log
import com.tecsup.catalogo.data.dto.HotelDto
import com.tecsup.catalogo.data.dto.toDomain
import com.tecsup.catalogo.data.dto.toEntity
import com.tecsup.catalogo.data.local.HotelDao
import com.tecsup.catalogo.data.local.HotelEntity
import com.tecsup.catalogo.data.remote.ApiService
import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class HotelRepositoryImpl(
    private val apiService: ApiService,
    private val hotelDao: HotelDao
) : HotelRepository {

    companion object {
        private const val TAG = "HotelRepository"
    }

    override val items: Flow<List<Hotel>> =
        hotelDao.todos().map { list -> list.map { it.toHotel() } }

    override val hotelesActivos: Flow<List<Hotel>> =
        hotelDao.todosActivos().map { list -> list.map { it.toHotel() } }

    override val favoritos: Flow<List<Hotel>> =
        hotelDao.favoritos().map { list -> list.map { it.toHotel() } }

    override suspend fun refrescar(): Result<Int> {
        return try {
            val remotos: List<HotelDto> = apiService.getHoteles()
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
            Log.e(TAG, "Error refrescando hoteles de MockAPI", e)
            Result.failure(e)
        }
    }

    override suspend fun obtenerDetalle(id: Int): Result<Hotel> {
        return try {
            val dto = apiService.getHotel(id.toString())
            val esFav = id in hotelDao.idsFavoritos()
            Result.success(dto.toDomain().copy(esFavorito = esFav))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val local = hotelDao.porId(id)
            if (local != null) Result.success(local.toHotel()) else Result.failure(e)
        }
    }

    override suspend fun alternarFavorito(id: Int) {
        hotelDao.alternarFavorito(id)
    }

    override suspend fun agregarHotel(hotel: Hotel): Result<Unit> {
        return try {
            val dto = HotelDto(
                id = if (hotel.id > 0) hotel.id.toString() else null,
                nombre = hotel.nombre,
                descripcion = hotel.descripcion,
                ciudad = hotel.ciudad,
                precioNoche = hotel.precioNoche,
                estrellas = hotel.estrellas,
                imagenUrl = hotel.imagenUrl,
                servicios = hotel.servicios,
                activo = hotel.activo
            )
            Log.d(TAG, "POST /hoteles -> $dto")
            val created = apiService.crearHotel(dto)
            Log.d(TAG, "POST /hoteles RESP -> $created")
            val entity = created.toEntity(esFavorito = false)
            hotelDao.insertarOActualizar(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error en agregarHotel remoto, guardando local", e)
            try {
                val entity = HotelEntity(
                    id = if (hotel.id > 0) hotel.id else (System.currentTimeMillis() % 100000).toInt(),
                    nombre = hotel.nombre,
                    descripcion = hotel.descripcion,
                    ciudad = hotel.ciudad,
                    precioNoche = hotel.precioNoche,
                    estrellas = hotel.estrellas,
                    imagenUrl = hotel.imagenUrl,
                    servicios = hotel.servicios.joinToString(","),
                    activo = hotel.activo,
                    esFavorito = false
                )
                hotelDao.insertarOActualizar(entity)
                Result.success(Unit)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

    override suspend fun actualizarHotel(hotel: Hotel): Result<Unit> {
        return try {
            val dto = HotelDto(
                id = hotel.id.toString(),
                nombre = hotel.nombre,
                descripcion = hotel.descripcion,
                ciudad = hotel.ciudad,
                precioNoche = hotel.precioNoche,
                estrellas = hotel.estrellas,
                imagenUrl = hotel.imagenUrl,
                servicios = hotel.servicios,
                activo = hotel.activo
            )
            Log.d(TAG, "PUT /hoteles/${hotel.id} -> $dto")
            try {
                val res = apiService.actualizarHotel(hotel.id.toString(), dto)
                Log.d(TAG, "PUT /hoteles/${hotel.id} RESP (200 OK) -> $res")
            } catch (e: HttpException) {
                Log.e(TAG, "PUT /hoteles/${hotel.id} HTTP ${e.code()}: ${e.response()?.errorBody()?.string()}", e)
                if (e.code() == 404) {
                    Log.w(TAG, "ID ${hotel.id} devolvio 404 en MockAPI, creando nuevo recurso remoto...")
                    val created = apiService.crearHotel(dto)
                    val newIdInt = created.id?.toIntOrNull() ?: 0
                    if (newIdInt > 0 && newIdInt != hotel.id) {
                        hotelDao.eliminarPorId(hotel.id)
                    }
                    val newEntity = created.toEntity(esFavorito = hotel.esFavorito)
                    hotelDao.insertarOActualizar(newEntity)
                    return Result.success(Unit)
                } else {
                    throw e
                }
            }

            val entity = hotelDao.porId(hotel.id)
            val esFav = entity?.esFavorito ?: false
            hotelDao.insertarOActualizar(hotel.toEntity(esFav))
            Result.success(Unit)
        } catch (e: Exception) {
            try {
                val entity = hotelDao.porId(hotel.id)
                val esFav = entity?.esFavorito ?: false
                hotelDao.insertarOActualizar(hotel.toEntity(esFav))
                Result.success(Unit)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

    override suspend fun eliminarHotel(id: Int): Result<Unit> {
        return try {
            try {
                Log.d(TAG, "DELETE /hoteles/$id")
                apiService.eliminarHotel(id.toString())
            } catch (e: Exception) {
                Log.e(TAG, "Error eliminando hotel $id en MockAPI", e)
            }
            hotelDao.eliminarPorId(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cambiarEstadoHotel(id: Int, activo: Boolean): Result<Unit> {
        return try {
            hotelDao.actualizarEstado(id, activo)
            val entity = hotelDao.porId(id) ?: return Result.failure(Exception("Hotel no encontrado"))

            val serviciosList = entity.servicios.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            val dto = HotelDto(
                id = id.toString(),
                nombre = entity.nombre,
                descripcion = entity.descripcion,
                ciudad = entity.ciudad,
                precioNoche = entity.precioNoche,
                estrellas = entity.estrellas,
                imagenUrl = entity.imagenUrl,
                servicios = serviciosList,
                activo = activo
            )

            Log.d(TAG, "PUT /hoteles/$id [cambiarEstado] -> $dto")
            try {
                val res = apiService.actualizarHotel(id.toString(), dto)
                Log.d(TAG, "PUT /hoteles/$id [cambiarEstado] RESP (200 OK) -> $res")
                Result.success(Unit)
            } catch (e: HttpException) {
                val errBody = e.response()?.errorBody()?.string() ?: ""
                Log.e(TAG, "PUT /hoteles/$id [cambiarEstado] HTTP ${e.code()}: $errBody", e)
                if (e.code() == 404) {
                    Log.w(TAG, "Hotel $id dio 404 en MockAPI al actualizar estado. Registrando en MockAPI...")
                    try {
                        val created = apiService.crearHotel(dto)
                        Log.d(TAG, "Hotel $id recreado exitosamente en MockAPI con nuevo id: '${created.id}'")
                        val newIdInt = created.id?.toIntOrNull() ?: 0
                        if (newIdInt > 0 && newIdInt != id) {
                            hotelDao.eliminarPorId(id)
                        }
                        val newEntity = created.toEntity(esFavorito = entity.esFavorito)
                        hotelDao.insertarOActualizar(newEntity)
                        Result.success(Unit)
                    } catch (createEx: Exception) {
                        Log.e(TAG, "Error registrando hotel $id en MockAPI tras 404", createEx)
                        hotelDao.actualizarEstado(id, !activo)
                        Result.failure(createEx)
                    }
                } else {
                    hotelDao.actualizarEstado(id, !activo)
                    Result.failure(e)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepcion no HTTP en PUT /hoteles/$id: ${e.message}", e)
                hotelDao.actualizarEstado(id, !activo)
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cantidadGuardada(): Int = hotelDao.contar()

    override suspend fun cantidadFavoritos(): Int = hotelDao.contarFavoritos()

    override suspend fun limpiarCache() {
        hotelDao.borrarTodo()
    }

    private fun HotelEntity.toHotel(): Hotel {
        return Hotel(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            ciudad = ciudad,
            precioNoche = precioNoche,
            estrellas = estrellas,
            imagenUrl = imagenUrl,
            esFavorito = esFavorito,
            servicios = servicios.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            activo = activo
        )
    }

    private fun Hotel.toEntity(esFavorito: Boolean): HotelEntity {
        return HotelEntity(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            ciudad = ciudad,
            precioNoche = precioNoche,
            estrellas = estrellas,
            imagenUrl = imagenUrl,
            esFavorito = esFavorito,
            servicios = servicios.joinToString(","),
            activo = activo
        )
    }
}
