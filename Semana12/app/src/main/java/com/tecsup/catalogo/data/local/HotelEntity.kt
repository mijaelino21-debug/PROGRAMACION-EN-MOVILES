package com.tecsup.catalogo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hoteles")
data class HotelEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val ciudad: String,
    val precioNoche: Double,
    val estrellas: Int,
    val imagenUrl: String,
    val esFavorito: Boolean = false
)