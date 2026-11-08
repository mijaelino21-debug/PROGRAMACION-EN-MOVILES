package com.tecsup.catalogo.data.mapper

import com.tecsup.catalogo.data.dto.HotelDto
import com.tecsup.catalogo.data.local.HotelEntity
import com.tecsup.catalogo.domain.model.Hotel

fun HotelDto.toEntity(esFavorito: Boolean = false): HotelEntity {
    return HotelEntity(
        id = id?.toIntOrNull() ?: 0,
        nombre = nombre ?: "Sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        ciudad = ciudad ?: "Sin ubicación",
        precioNoche = precioNoche ?: 0.0,
        estrellas = estrellas ?: 1,
        imagenUrl = if (!imagenUrl.isNullOrEmpty()) imagenUrl else "https://picsum.photos/300/200",
        esFavorito = esFavorito
    )
}

fun HotelEntity.toDomain(): Hotel {
    return Hotel(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        ciudad = ciudad,
        precioNoche = precioNoche,
        estrellas = estrellas,
        imagenUrl = imagenUrl,
        esFavorito = esFavorito
    )
}