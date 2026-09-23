package com.lino.tecsupfit.model

data class ReservaModel(
    val id: String,
    val nombreClase: String,
    val horario: String,
    val sala: String,
    var estado: String
)