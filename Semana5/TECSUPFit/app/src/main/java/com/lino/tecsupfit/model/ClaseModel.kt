package com.lino.tecsupfit.model

data class ClaseModel(
    val id: String,
    val nombre: String,
    val horario: String,
    val sala: String,
    val duracion: String,
    val descripcion: String,
    val cuposDisponibles: Int,
    val totalCupos: Int,
    val categoria: String
)