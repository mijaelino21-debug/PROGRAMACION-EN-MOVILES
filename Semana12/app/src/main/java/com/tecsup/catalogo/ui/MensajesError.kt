package com.tecsup.catalogo.ui

import retrofit2.HttpException
import java.io.IOException

fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "No hay conexión a internet"
    is HttpException -> "El servidor respondió con error ${e.code()}"
    else -> e.localizedMessage ?: e.message ?: "Ocurrió un error inesperado"
}
