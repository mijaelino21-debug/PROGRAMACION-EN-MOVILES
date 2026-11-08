package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.data.dto.HotelDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("hoteles")
    suspend fun getHoteles(): List<HotelDto>

    @GET("hoteles/{id}")
    suspend fun getHotel(@Path("id") id: Int): HotelDto
}