package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.data.dto.HotelDto
import retrofit2.http.GET

interface ApiService {
    @GET("hoteles")
    suspend fun getHoteles(): List<HotelDto>
}