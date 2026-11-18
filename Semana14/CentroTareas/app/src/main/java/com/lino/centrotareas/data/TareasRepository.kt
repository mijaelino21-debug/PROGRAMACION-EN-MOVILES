package com.lino.centrotareas.data

import kotlinx.coroutines.flow.Flow

data class ProgresoPrimos(
    val progreso: Float,
    val cantidad: Int
)

interface TareasRepository {
    fun contarPrimos(hasta: Int): Flow<ProgresoPrimos>
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
    fun obtenerHistorial(): Flow<List<String>>
    suspend fun guardarResultado(resultado: String)
}
