package com.Lino.lab02carritokotlin

abstract class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    abstract fun calcularImporte(): Double

    open fun mostrarInfo(): String {
        return String.format("%-20s x%d S/ %8.2f", nombre, cantidad, calcularImporte())
    }
}
class ProductoElectronico(
    nombre: String, precio: Double, cantidad: Int, val garantiaMeses: Int
) : Producto(nombre, precio, cantidad) {
    override fun calcularImporte(): Double = precio * cantidad
    override fun mostrarInfo(): String {
        return super.mostrarInfo() + "  (Garantia: $garantiaMeses meses)"
    }
}
class ProductoAccesorio(
    nombre: String, precio: Double, cantidad: Int
) : Producto(nombre, precio, cantidad) {
    override fun calcularImporte(): Double = precio * cantidad
}
class CarritoPOO {
    private val productos = mutableListOf<Producto>()
    fun calcularSubtotal(): Double = productos.sumOf { it.calcularImporte() }
    fun calcularIGV(subtotal: Double): Double = subtotal * 0.18
    fun calcularTotal(subtotal: Double, igv: Double): Double = subtotal + igv
    fun calcularDescuento(total: Double): Double = when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
    fun mostrarDetalle() {
        println("---------------- DETALLE DEL CARRITO ----------------")
        productos.forEachIndexed { i, p -> println("${i + 1}. ${p.mostrarInfo()}") }
        println("-----------------------------------------------------")
    }
    fun productoMasCaro(): Producto? = productos.maxByOrNull { it.precio }
    fun cantidadProductos(): Int = productos.size

    fun agregar(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")

    val nombreCliente = "Mijael Lino"


    println("Cliente: $nombreCliente")
    println()

    println("Gracias por su compra, $nombreCliente!")
}
