package com.tecsup.catalogo.data.dto

import com.tecsup.catalogo.data.local.HotelEntity
import com.tecsup.catalogo.domain.model.Hotel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HotelDto(
    @SerialName("id") val id: String? = null,
    @SerialName("nombre") val nombre: String? = null,
    @SerialName("descripcion") val descripcion: String? = null,
    @SerialName("ciudad") val ciudad: String? = null,
    @SerialName("precioNoche") val precioNoche: Double? = null,
    @SerialName("estrellas") val estrellas: Int? = null,
    @SerialName("imagenUrl") val imagenUrl: String? = null,
    @SerialName("servicios") val servicios: List<String>? = null,
    @SerialName("activo") val activo: Boolean? = true
)

fun HotelDto.toDomain(): Hotel {
    val defaultServicios = listOf("Wi-Fi gratis", "Piscina", "Aire acondicionado", "Restaurante", "Recepción 24/7")
    return Hotel(
        id = id?.toIntOrNull() ?: 0,
        nombre = nombre ?: "Sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        ciudad = ciudad ?: "Sin ubicación",
        precioNoche = precioNoche ?: 0.0,
        estrellas = estrellas ?: 1,
        imagenUrl = if (!imagenUrl.isNullOrEmpty()) imagenUrl else "https://picsum.photos/300/200",
        esFavorito = false,
        servicios = if (servicios.isNullOrEmpty()) defaultServicios else servicios,
        activo = activo ?: true
    )
}

fun HotelDto.toEntity(esFavorito: Boolean = false): HotelEntity {
    val defaultServicios = listOf("Wi-Fi gratis", "Piscina", "Aire acondicionado", "Restaurante", "Recepción 24/7")
    val servList = if (servicios.isNullOrEmpty()) defaultServicios else servicios
    return HotelEntity(
        id = id?.toIntOrNull() ?: 0,
        nombre = nombre ?: "Sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        ciudad = ciudad ?: "Sin ubicación",
        precioNoche = precioNoche ?: 0.0,
        estrellas = estrellas ?: 1,
        imagenUrl = if (!imagenUrl.isNullOrEmpty()) imagenUrl else "https://picsum.photos/300/200",
        esFavorito = esFavorito,
        servicios = servList.joinToString(","),
        activo = activo ?: true
    )
}
