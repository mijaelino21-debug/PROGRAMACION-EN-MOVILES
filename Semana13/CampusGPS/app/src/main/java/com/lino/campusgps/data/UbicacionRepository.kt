package com.lino.campusgps.data

import com.lino.campusgps.model.Posicion

class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")

interface UbicacionRepository {
    fun playServicesDisponible(): Boolean
    suspend fun obtenerPosicion(): Result<Posicion>
}
