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

    fun buscarProducto(nombre: String): Producto? {
        return productos.find { it.nombre == nombre }
    }

    fun eliminarProducto(nombre: String) {
        productos.removeIf { it.nombre == nombre }
    }
}


fun main() {
    val scanner = java.util.Scanner(System.`in`)

    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")

    print("Ingrese el nombre del cliente: ")
    val nombreCliente = scanner.nextLine()
    val carrito = CarritoPOO()

    print("¿Cuántos productos desea ingresar al carrito?: ")
    val totalIngresos = scanner.nextInt()
    scanner.nextLine()

    for (i in 1..totalIngresos) {
        println("\n--- Producto #$i ---")
        print("Tipo (1: Electrónico, 2: Accesorio): ")
        val tipo = scanner.nextInt()
        scanner.nextLine()

        print("Nombre: ")
        val nombre = scanner.nextLine()

        print("Precio (S/): ")
        val precio = scanner.nextDouble()

        print("Cantidad: ")
        val cantidad = scanner.nextInt()

        if (tipo == 1) {
            print("Meses de garantía: ")
            val garantia = scanner.nextInt()

            carrito.agregar(ProductoElectronico(nombre, precio, cantidad, garantia))
        } else {
            carrito.agregar(ProductoAccesorio(nombre, precio, cantidad))
        }
    }

    println()
    carrito.mostrarDetalle()

    println()
    print("Ingrese el nombre del producto que desea buscar: ")
    val nombreBuscar = scanner.nextLine()
    val encontrado = carrito.buscarProducto(nombreBuscar)
    if (encontrado != null) {
        println("Producto encontrado: ${encontrado.nombre} - S/ ${String.format("%.2f", encontrado.precio)}")
    } else {
        println("Producto no encontrado")
    }

    println()
    print("Ingrese el nombre del producto que desea eliminar: ")
    val nombreEliminar = scanner.nextLine()
    carrito.eliminarProducto(nombreEliminar)
    println("Producto eliminado: $nombreEliminar")
    println()
    carrito.mostrarDetalle()

    println("Cantidad de productos : ${carrito.cantidadProductos()}")

    val subtotal = carrito.calcularSubtotal()
    val igv = carrito.calcularIGV(subtotal)
    val total = carrito.calcularTotal(subtotal, igv)

    println(String.format("%-22s: S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", igv))
    println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", total))

    println()
    val masCaro = carrito.productoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/%.2f)", masCaro.precio))
    }

    val descuento = carrito.calcularDescuento(total)
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