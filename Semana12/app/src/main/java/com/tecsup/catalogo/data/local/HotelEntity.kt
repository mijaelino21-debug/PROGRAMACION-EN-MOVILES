package com.tecsup.catalogo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hoteles")
data class HotelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteId: String? = null,
    val nombre: String,
    val descripcion: String,
    val ciudad: String,
    val precioNoche: Double,
    val estrellas: Int,
    val imagenUrl: String,
    val esFavorito: Boolean = false,
    val servicios: String = "Wi-Fi gratis,Piscina,Aire acondicionado,Restaurante,Recepción 24/7",
    val activo: Boolean = true
)