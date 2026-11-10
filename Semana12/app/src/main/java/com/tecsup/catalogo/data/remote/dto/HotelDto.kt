package com.tecsup.catalogo.data.remote.dto

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
    val defaultServicios = listOf(
        "Wi-Fi gratis",
        "Piscina",
        "Aire acondicionado",
        "Restaurante",
        "Recepción 24/7"
    )

    return Hotel(
        id = 0,
        nombre = nombre ?: "Sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        ciudad = ciudad ?: "Sin ubicación",
        precioNoche = precioNoche ?: 0.0,
        estrellas = estrellas ?: 1,
        imagenUrl = imagenUrl?.takeIf { it.isNotBlank() }
            ?: "https://picsum.photos/300/200",
        esFavorito = false,
        servicios = servicios?.takeIf { it.isNotEmpty() } ?: defaultServicios,
        activo = activo ?: true,
        remoteId = id
    )
}

fun HotelDto.toEntity(esFavorito: Boolean = false): HotelEntity {
    val defaultServicios = listOf(
        "Wi-Fi gratis",
        "Piscina",
        "Aire acondicionado",
        "Restaurante",
        "Recepción 24/7"
    )

    val servList = servicios?.takeIf { it.isNotEmpty() } ?: defaultServicios

    return HotelEntity(
        remoteId = id,
        nombre = nombre ?: "Sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        ciudad = ciudad ?: "Sin ubicación",
        precioNoche = precioNoche ?: 0.0,
        estrellas = estrellas ?: 1,
        imagenUrl = imagenUrl?.takeIf { it.isNotBlank() }
            ?: "https://picsum.photos/300/200",
        esFavorito = esFavorito,
        servicios = servList.joinToString(","),
        activo = activo ?: true
    )
}
