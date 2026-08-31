package com.lino.libreria

import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val MULTA_POR_DIA = 1.50
val FORMATO_FECHA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

data class Prestamo(
    val titulo: String,
    val tipoUsuario: String,
    val fechaPrestamo: String,
    val fechaDevolucion: String,
    val fechaEntrega: String,
    val diasAtraso: Int
)

fun main() {
    println("=========================================")
    println(" SISTEMA DE MULTAS - POO")
    println("=========================================")
}