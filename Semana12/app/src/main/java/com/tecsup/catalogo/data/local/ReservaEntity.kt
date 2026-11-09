package com.tecsup.catalogo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val usuarioId: String,
    val hotelId: Int,
    val nombreHotel: String,
    val imagenUrl: String = "",
    val ciudad: String = "",
    val fechaEntrada: String,
    val fechaSalida: String,
    val cantidadHuespedes: Int,
    val precioPorNoche: Double,
    val cantidadNoches: Int,
    val precioTotal: Double,
    val estado: String = "PENDIENTE", // PENDIENTE, CONFIRMADA, RECHAZADA, CANCELADA
    val motivoRechazo: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)
