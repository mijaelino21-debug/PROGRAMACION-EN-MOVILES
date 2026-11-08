package com.tecsup.catalogo.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    // Tu URL base real de MockAPI (debe terminar en '/')
    private const val BASE_URL = "https://6aed207ffc3b911e6fd5f209.mockapi.io/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}