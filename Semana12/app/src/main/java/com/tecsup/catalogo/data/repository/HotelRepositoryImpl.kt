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

    // ---------------------------------------------------------------
    // Sincronización: Room es la fuente de verdad, MockAPI la actualiza
    // ---------------------------------------------------------------
    override suspend fun refrescar(): Result<Int> {
        return try {
            // Si MockAPI tiene copias (mismo nombre y ciudad), nos quedamos con la de id más bajo
            val remotos: List<HotelDto> = apiService.getHoteles()
                .sortedBy { it.id?.toIntOrNull() ?: Int.MAX_VALUE }
                .distinctBy { "${it.nombre}|${it.ciudad}" }

            // Para reparar filas viejas sin remoteId: nombre+ciudad -> id remoto
            val remotoPorNombre = HashMap<String, String>()
            remotos.forEach { dto ->
                val id = dto.id ?: return@forEach
                remotoPorNombre.putIfAbsent("${dto.nombre}|${dto.ciudad}", id)
            }
            val idsRemotos = remotos.mapNotNull { it.id }.toSet()

            // 1) Agrupar filas locales por hotel remoto y borrar las repetidas
            val existentes = hotelDao.listarTodos()
            val conservar = HashMap<String, HotelEntity>()
            val sobrantes = mutableListOf<Int>()
            existentes
                .groupBy { it.remoteId ?: remotoPorNombre["${it.nombre}|${it.ciudad}"] }
                .forEach { (clave, lista) ->
                    if (clave == null) return@forEach // hotel solo local: no se toca
                    val keep = lista.firstOrNull { it.esFavorito } ?: lista.minBy { it.id }
                    conservar[clave] = keep
                    lista.filter { it.id != keep.id }.forEach { sobrantes.add(it.id) }
                }

            // Filas con remoteId que ya no existe en el servidor (copias o borrados)
            existentes
                .filter { it.remoteId != null && it.remoteId !in idsRemotos }
                .forEach { sobrantes.add(it.id) }

            if (sobrantes.isNotEmpty()) hotelDao.eliminarPorIds(sobrantes.distinct())

            // 2) Actualizar los existentes (mismo id local) y agregar los nuevos
            val entidades = remotos.map { dto ->
                val base = dto.toEntity()
                val previo = dto.id?.let { conservar[it] }
                if (previo != null) {
                    base.copy(
                        id = previo.id,
                        esFavorito = previo.esFavorito,
                        activo = previo.activo
                    )
                } else base
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

    // ---------------------------------------------------------------
    // Detalle (segundo endpoint: GET hoteles/{id})
    // ---------------------------------------------------------------
    override suspend fun obtenerDetalle(id: Int): Result<Hotel> {
        val local = hotelDao.porId(id)
        return try {
            val remoteId = local?.remoteId ?: id.toString()
            val dto = apiService.getHotel(remoteId)
            val esFav = local?.esFavorito ?: false
            Result.success(
                dto.toDomain().copy(
                    id = id,
                    esFavorito = esFav,
                    activo = local?.activo ?: (dto.activo ?: true)
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Sin internet: usamos lo guardado en Room
            if (local != null) Result.success(local.toHotel()) else Result.failure(e)
        }
    }

    override suspend fun alternarFavorito(id: Int) {
        hotelDao.alternarFavorito(id)
    }

    // ---------------------------------------------------------------
    // Administración de hoteles
    // ---------------------------------------------------------------
    override suspend fun agregarHotel(hotel: Hotel): Result<Unit> {
        return try {
            val dto = hotel.toDto(remoteId = null)
            Log.d(TAG, "POST /hoteles -> $dto")
            val created = apiService.crearHotel(dto)
            hotelDao.insertarOActualizar(created.toEntity(esFavorito = false))
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error en agregarHotel remoto, guardando local", e)
            try {
                hotelDao.insertarOActualizar(hotel.toEntity(esFavorito = false, remoteId = null).copy(id = 0))
                Result.success(Unit)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

    override suspend fun actualizarHotel(hotel: Hotel): Result<Unit> {
        return try {
            val previo = hotelDao.porId(hotel.id)
            val local = hotel.toEntity(
                esFavorito = previo?.esFavorito ?: false,
                remoteId = previo?.remoteId
            )
            // Se guarda primero en Room (la app funciona aunque no haya internet)
            hotelDao.insertarOActualizar(local)
            try {
                subirARemoto(local)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "No se pudo actualizar en MockAPI; queda guardado local", e)
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun eliminarHotel(id: Int): Result<Unit> {
        return try {
            val remoteId = hotelDao.porId(id)?.remoteId
            if (remoteId != null) {
                try {
                    Log.d(TAG, "DELETE /hoteles/$remoteId")
                    apiService.eliminarHotel(remoteId)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.e(TAG, "Error eliminando hotel $remoteId en MockAPI", e)
                }
            }
            hotelDao.eliminarPorId(id)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cambiarEstadoHotel(id: Int, activo: Boolean): Result<Unit> {
        return try {
            hotelDao.porId(id) ?: return Result.failure(Exception("Hotel no encontrado"))
            hotelDao.actualizarEstado(id, activo)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------
    // Estadísticas y caché
    // ---------------------------------------------------------------
    override suspend fun cantidadGuardada(): Int = hotelDao.contar()

    override suspend fun cantidadFavoritos(): Int = hotelDao.contarFavoritos()

    override suspend fun limpiarCache() {
        hotelDao.borrarTodo()
    }

    // ---------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------

    /** Sube el hotel a MockAPI: PUT si ya tiene remoteId; POST si no existe (o dio 404). */
    private suspend fun subirARemoto(local: HotelEntity) {
        val remoteId = local.remoteId
        val resultado: HotelDto = if (remoteId == null) {
            apiService.crearHotel(local.toDto(remoteId = null))
        } else {
            try {
                apiService.actualizarHotel(remoteId, local.toDto(remoteId))
            } catch (e: HttpException) {
                if (e.code() == 404) apiService.crearHotel(local.toDto(remoteId = null)) else throw e
            }
        }
        hotelDao.insertarOActualizar(local.copy(remoteId = resultado.id ?: remoteId))
    }

    private fun HotelEntity.toDto(remoteId: String?): HotelDto = HotelDto(
        id = remoteId,
        nombre = nombre,
        descripcion = descripcion,
        ciudad = ciudad,
        precioNoche = precioNoche,
        estrellas = estrellas,
        imagenUrl = imagenUrl,
        servicios = servicios.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        activo = activo
    )

    private fun Hotel.toDto(remoteId: String?): HotelDto = HotelDto(
        id = remoteId,
        nombre = nombre,
        descripcion = descripcion,
        ciudad = ciudad,
        precioNoche = precioNoche,
        estrellas = estrellas,
        imagenUrl = imagenUrl,
        servicios = servicios,
        activo = activo
    )

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
            activo = activo,
            remoteId = remoteId
        )
    }

    private fun Hotel.toEntity(esFavorito: Boolean, remoteId: String?): HotelEntity {
        return HotelEntity(
            id = id,
            remoteId = remoteId,
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