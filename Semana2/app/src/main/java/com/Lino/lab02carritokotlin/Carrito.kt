package com.Lino.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
abstract class ProductoPOO(
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
) : ProductoPOO(nombre, precio, cantidad) {
    override fun calcularImporte(): Double = precio * cantidad
}
class ProductoAccesorio(
    nombre: String, precio: Double, cantidad: Int
) : ProductoPOO(nombre, precio, cantidad) {
    override fun calcularImporte(): Double = precio * cantidad
}
class CarritoPOO {
    private val productos = mutableListOf<ProductoPOO>()
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
    fun productoMasCaro(): ProductoPOO? = productos.maxByOrNull { it.precio }
    fun cantidadProductos(): Int = productos.size

    fun agregar(producto: ProductoPOO) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")

    val nombreCliente = "Mijael Lino"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 25.0, 3))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println()
    mostrarDetalle(carrito)
    println("Cantidad de productos : ${carrito.size}")

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-22s: S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", igv))
    println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", total))

    println()
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/%.2f)", masCaro.precio))
    }

    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    if (total > 5000) {
        println("Descuento aplicado: 10% por compra mayor a S/ 5000")
    } else if (total > 3000) {
        println("Descuento aplicado: 5% por compra mayor a S/ 3000")
    } else {
        println("Descuento aplicado: Sin descuento")
    }

    println(String.format("%-22s: S/ %8.2f", "TOTAL CON DESCUENTO", totalConDescuento))
    println()
    println("Gracias por su compra, $nombreCliente!")
}

    fun calcularSubtotal(productos: List<Producto>): Double {
        var subtotal = 0.0
        for (p in productos) {
            subtotal += p.precio * p.cantidad
        }
        return subtotal
    }

    fun calcularIGV(subtotal: Double): Double {
        return subtotal * 0.18
    }

    fun calcularTotal(subtotal: Double, igv: Double): Double {
        return subtotal + igv
    }
fun mostrarDetalle(productos: List<Producto>) {
    println("---------------- DETALLE DEL CARRITO ----------------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("-----------------------------------------------------")
}
fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}