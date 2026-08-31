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

    fun calcularMultaTotal(): Double = diasAtraso * obtenerMultaPorDia()

    fun obtenerEstado(): String = when {
        diasAtraso > 0 -> "Devuelto con $diasAtraso dia(s) de atraso"
        else -> "Devuelto a tiempo"
    }

    fun mostrarTablaMultas() {
        val tarifa = obtenerMultaPorDia()
        when {
            diasAtraso > 0 -> {
                println("\nDia\tFecha\tMulta/Dia\tAcumulado")
                var acumulado = 0.0
                for (dia in 1..diasAtraso) {
                    acumulado += tarifa
                    val fechaDia = "${17 + dia}/10"
                    println("$dia\t$fechaDia\tS/ $tarifa\t\tS/ $acumulado")
                }
            }
        }
    }
}

fun main() {
    println("=========================================")
    println(" SISTEMA DE MULTAS - POO")
    println("=========================================")
    print("Titulo Libro: ")
    val titulo = readLine() ?: ""

    print("Tipo de Usuario (Docente/Alumno): ")
    val tipoUsuario = readLine() ?: ""

    print("Fecha Prestamo: ")
    val fechaPrestamo = readLine() ?: ""

    print("Fecha Devolucion: ")
    val fechaDevolucion = readLine() ?: ""

    print("Fecha Entrega: ")
    val fechaEntrega = readLine() ?: ""

    print("Dias de Atraso: ")
    val diasAtraso = readLine()?.toIntOrNull() ?: 0
    val prestamo = Prestamo(titulo, tipoUsuario, fechaPrestamo, fechaDevolucion, fechaEntrega, diasAtraso)

    println()
    println("TITULO LIBRO     : ${prestamo.titulo}")
    println("TIPO DE USUARIO  : ${prestamo.tipoUsuario}")
    println("FECHA PRESTAMO   : ${prestamo.fechaPrestamo}")
    println("FECHA DEVOLUCION : ${prestamo.fechaDevolucion}")
    println("FECHA ENTREGA    : ${prestamo.fechaEntrega}")
    println("ESTADO           : ${prestamo.obtenerEstado()}")

    prestamo.mostrarTablaMultas()

    println()
    println("MULTA TOTAL S/ ${prestamo.calcularMultaTotal()}")

}
