package com.lino.libreria

const val MULTA_POR_DIA = 1.50

data class Prestamo(
    val titulo: String,
    val tipoUsuario: String,
    val fechaPrestamo: String,
    val fechaDevolucion: String,
    val fechaEntrega: String,
    val diasAtraso: Int
) {
    fun calcularMultaTotal(): Double {
        var acumulado = 0.0
        for (dia in 1..diasAtraso) {
            acumulado += MULTA_POR_DIA
        }
        return acumulado
    }

    fun obtenerEstado(): String = when {
        diasAtraso > 0 -> "Devuelto con $diasAtraso dia(s) de atraso"
        else -> "Devuelto a tiempo"
    }
}

fun main() {
    println("=========================================")
    println(" SISTEMA DE MULTAS - POO")
    println("=========================================")
}