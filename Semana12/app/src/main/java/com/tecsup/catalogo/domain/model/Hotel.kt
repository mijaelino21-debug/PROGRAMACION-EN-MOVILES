package com.tecsup.catalogo.domain.model

data class Hotel(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val ciudad: String,
    val precioNoche: Double,
    val estrellas: Int,
    val imagenUrl: String,
    val esFavorito: Boolean = false
)