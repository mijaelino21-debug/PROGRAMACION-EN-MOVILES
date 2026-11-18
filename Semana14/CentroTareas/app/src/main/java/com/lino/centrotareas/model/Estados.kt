package com.lino.centrotareas.model

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data class Calculando(val progreso: Float = 0f) : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data object Cancelado : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi

    data class Error(val mensaje: String) : CargaUi
}

data class EstadisticaModo(
    val modalidad: String,
    val cantidadEjecuciones: Int = 0,
    val tiempoTotalMs: Long = 0L,
    val ultimoTiempoMs: Long = 0L
) {
    val tiempoPromedioMs: Double
        get() = if (cantidadEjecuciones > 0) tiempoTotalMs.toDouble() / cantidadEjecuciones else 0.0
}
