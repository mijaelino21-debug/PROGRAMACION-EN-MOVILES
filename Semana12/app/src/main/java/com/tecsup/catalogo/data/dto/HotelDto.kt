package com.tecsup.catalogo.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HotelDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val ciudad: String,
    val precioNoche: Double,
    val estrellas: Int,
    val imagenUrl: String
)
