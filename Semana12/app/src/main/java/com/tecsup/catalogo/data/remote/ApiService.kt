package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.data.remote.dto.HotelDto
import retrofit2.http.*

interface ApiService {
    @GET("hoteles")
    suspend fun getHoteles(): List<HotelDto>

    @GET("hoteles/{id}")
    suspend fun getHotel(@Path("id") id: String): HotelDto

    @POST("hoteles")
    suspend fun crearHotel(@Body hotel: HotelDto): HotelDto

    @PUT("hoteles/{id}")
    suspend fun actualizarHotel(@Path("id") id: String, @Body hotel: HotelDto): HotelDto

    @DELETE("hoteles/{id}")
    suspend fun eliminarHotel(@Path("id") id: String): HotelDto
}
