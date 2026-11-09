package com.tecsup.catalogo.data.mapper

import com.tecsup.catalogo.data.local.HotelEntity
import com.tecsup.catalogo.data.local.ReservaEntity
import com.tecsup.catalogo.domain.model.Hotel
import com.tecsup.catalogo.domain.model.Reserva

fun HotelEntity.toDomain(): Hotel {
    val defaultServicios = listOf("Wi-Fi gratis", "Piscina", "Aire acondicionado", "Restaurante", "Recepción 24/7")
    val servList = if (servicios.isBlank()) defaultServicios else servicios.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    return Hotel(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        ciudad = ciudad,
        precioNoche = precioNoche,
        estrellas = estrellas,
        imagenUrl = imagenUrl,
        esFavorito = esFavorito,
        servicios = servList
    )
}

fun ReservaEntity.toDomain() = Reserva(
    id = id,
    usuarioId = usuarioId,
    hotelId = hotelId,
    nombreHotel = nombreHotel,
    imagenUrl = imagenUrl,
    ciudad = ciudad,
    fechaEntrada = fechaEntrada,
    fechaSalida = fechaSalida,
    cantidadHuespedes = cantidadHuespedes,
    precioPorNoche = precioPorNoche,
    cantidadNoches = cantidadNoches,
    precioTotal = precioTotal,
    estado = estado,
    motivoRechazo = motivoRechazo,
    fechaCreacion = fechaCreacion
)

fun Reserva.toEntity() = ReservaEntity(
    id = id,
    usuarioId = usuarioId,
    hotelId = hotelId,
    nombreHotel = nombreHotel,
    imagenUrl = imagenUrl,
    ciudad = ciudad,
    fechaEntrada = fechaEntrada,
    fechaSalida = fechaSalida,
    cantidadHuespedes = cantidadHuespedes,
    precioPorNoche = precioPorNoche,
    cantidadNoches = cantidadNoches,
    precioTotal = precioTotal,
    estado = estado,
    motivoRechazo = motivoRechazo,
    fechaCreacion = fechaCreacion
)
