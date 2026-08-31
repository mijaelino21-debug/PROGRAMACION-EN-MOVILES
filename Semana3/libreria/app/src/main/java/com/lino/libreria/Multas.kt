package com.lino.libreria

data class Prestamo(
    val titulo: String,
    val tipoUsuario: String,
    val fechaPrestamo: String,
    val fechaDevolucion: String,
    val fechaEntrega: String,
    val diasAtraso: Int
) {
    fun obtenerMultaPorDia(): Double = when (tipoUsuario.lowercase().trim()) {
        "docente", "profesor", "profesora" -> 3.00
        else -> 1.50
    }

    fun calcularMultaTotal(): Double {
        val tarifa = obtenerMultaPorDia()
        var acumulado = 0.0
        for (dia in 1..diasAtraso) {
            acumulado += tarifa
        }
        return acumulado
    }

    fun obtenerEstado(): String = when {
        diasAtraso > 0 -> "Devuelto con $diasAtraso dia(s) de atraso"
        else -> "Devuelto a tiempo"
    }

    fun mostrarTablaMultas() {
        val tarifa = obtenerMultaPorDia()
        when {
            diasAtraso == 0 -> println("\n¡No hay multas aplicables! El libro fue entregado a tiempo.")
            else -> {
                println("\nDia\tFecha\t\tMulta/Dia\tAcumulado")
                var acumulado = 0.0
                for (dia in 1..diasAtraso) {
                    acumulado += tarifa
                    val fechaDia = "${17 + dia}/10"
                    println(String.format("%d\t%s\t\tS/ %.2f\t\tS/ %.2f", dia, fechaDia, tarifa, acumulado))
                }
            }
        }
    }
}

fun main() {
    println("=========================================")
    println(" SISTEMA DE MULTAS - POO")
    println("=========================================")
}