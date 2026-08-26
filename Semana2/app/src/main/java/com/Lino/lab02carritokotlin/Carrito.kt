package com.Lino.lab02carritokotlin

import java.util.Scanner

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
    val scanner = Scanner(System.`in`)

    // 1. CATÁLOGO BASE DE LA TIENDA (5 Electrónicos y 5 Accesorios)
    val catalogo = listOf(
        // Electrónicos (Nombre, Precio, Garantía base)
        ProductoElectronico("Laptop HP", 2500.0, 1, garantiaMeses = 12),
        ProductoElectronico("Monitor Samsung 24", 650.0, 1, garantiaMeses = 24),
        ProductoElectronico("Teclado Mecanico ASUS", 350.0, 1, garantiaMeses = 12),
        ProductoElectronico("Tablet Lenovo", 800.0, 1, garantiaMeses = 6),
        ProductoElectronico("Consola PlayStation 5", 2800.0, 1, garantiaMeses = 12),

        // Accesorios (Nombre, Precio)
        ProductoAccesorio("Mouse Logitech", 45.5, 1),
        ProductoAccesorio("Audifonos Razer", 180.0, 1),
        ProductoAccesorio("USB Kingston 64GB", 25.0, 1),
        ProductoAccesorio("Pad Mouse Gamer", 35.0, 1),
        ProductoAccesorio("Soporte para Laptop", 60.0, 1)
    )

    val carrito = CarritoPOO()

    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")

    print("Ingrese su nombre (Cliente): ")
    val nombreCliente = scanner.nextLine()
    println("\n¡Bienvenido/a $nombreCliente!")

    // 2. MOSTRAR CATÁLOGO DISPONIBLE
    println("\n----------------- CATÁLOGO DE PRODUCTOS -----------------")
    catalogo.forEachIndexed { i, p ->
        val tipo = if (p is ProductoElectronico) "Electrónico (Garantía: ${p.garantiaMeses} meses)" else "Accesorio"
        println("${i + 1}. %-25s - S/ %8.2f [%s]".format(p.nombre, p.precio, tipo))
    }
    println("---------------------------------------------------------")

    // 3. SELECCIÓN DE PRODUCTOS POR EL CLIENTE
    print("\n¿Cuántos productos diferentes desea agregar al carrito?: ")
    val totalSelecciones = scanner.nextInt()
    scanner.nextLine() // Limpiar búfer

    for (i in 1..totalSelecciones) {
        println("\n--- Selección #$i ---")
        print("Ingrese el número del producto (1 a 10): ")
        val opcion = scanner.nextInt()
        scanner.nextLine() // Limpiar búfer

        if (opcion in 1..catalogo.size) {
            val productoElegido = catalogo[opcion - 1]

            print("Ingrese la cantidad para '${productoElegido.nombre}': ")
            val cantidad = scanner.nextInt()
            scanner.nextLine() // Limpiar búfer

            // Instanciamos el producto con el precio fijo del catálogo y la cantidad elegida
            if (productoElegido is ProductoElectronico) {
                carrito.agregar(ProductoElectronico(productoElegido.nombre, productoElegido.precio, cantidad, productoElegido.garantiaMeses))
            } else {
                carrito.agregar(ProductoAccesorio(productoElegido.nombre, productoElegido.precio, cantidad))
            }
        } else {
            println("Opción no válida. Se omitió esta selección.")
        }
    }

    println()
    carrito.mostrarDetalle()

    // 4. BÚSQUEDA INTERACTIVA EN EL CARRITO
    println()
    print("¿Desea buscar un producto en su carrito? (Escriba el nombre o presione Enter para omitir): ")
    val nombreBuscar = scanner.nextLine()
    if (nombreBuscar.isNotBlank()) {
        val encontrado = carrito.buscarProducto(nombreBuscar)
        if (encontrado != null) {
            println("-> Producto encontrado en tu carrito: ${encontrado.nombre} - S/ ${String.format("%.2f", encontrado.precio)}")
        } else {
            println("-> El producto '$nombreBuscar' no está en tu carrito.")
        }
    }

    // 5. ELIMINACIÓN INTERACTIVA EN EL CARRITO
    println()
    print("¿Desea eliminar algún producto del carrito antes de pagar? (Escriba el nombre o Enter para omitir): ")
    val nombreEliminar = scanner.nextLine()
    if (nombreEliminar.isNotBlank()) {
        carrito.eliminarProducto(nombreEliminar)
        println("-> Se eliminó '$nombreEliminar' del carrito.")
        println()
        carrito.mostrarDetalle()
    }

    // 6. BOLETA FINAL Y CÁLCULOS
    println("\n============== BOLETA DE VENTA ==============")
    println("Cliente: $nombreCliente")
    println("Cantidad de productos distintos: ${carrito.cantidadProductos()}")

    val subtotal = carrito.calcularSubtotal()
    val igv = carrito.calcularIGV(subtotal)
    val total = carrito.calcularTotal(subtotal, igv)

    println(String.format("%-25s: S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-25s: S/ %8.2f", "IGV (18%)", igv))
    println(String.format("%-25s: S/ %8.2f", "TOTAL A PAGAR", total))

    println()
    val masCaro = carrito.productoMasCaro()
    if (masCaro != null) {
        println("Producto más caro llevado: ${masCaro.nombre} (S/ ${String.format("%.2f", masCaro.precio)})")
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

    println(String.format("%-25s: S/ %8.2f", "TOTAL CON DESCUENTO", totalConDescuento))
    println("=============================================")
    println("¡Gracias por su compra, $nombreCliente!")
}