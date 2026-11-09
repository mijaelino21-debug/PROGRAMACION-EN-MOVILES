package com.tecsup.catalogo.domain.model

data class Reserva(
    val id: Long = 0L,
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
    val estado: String = "PENDIENTE",
    val motivoRechazo: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)
