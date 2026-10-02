package com.tecsup.mibodega.ui.cliente.modelo

data class Pedido(
    val numero: Int,
    val total: Double,
    val direccion: String,
    val referencia: String,
    val metodoPago: String
)