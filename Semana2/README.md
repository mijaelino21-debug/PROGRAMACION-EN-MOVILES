# Lab02 - Carrito de Compras (Kotlin)

Mijael Lino

Programa de consola que simula un carrito de compras: agrega productos, calcula subtotal, IGV y total, aplica descuentos por monto, busca y elimina productos.


- **Rama-sin-IA**: versión funcional del carrito, usando funciones y una data class simple.

## ¿Por qué nombre y precio son val pero cantidad es var? ¿Qué
pasaría si intentas cambiar el precio después de crear el producto?

nombre y precio son val porque no deben cambiar una vez creado el producto, mientras que cantidad es var porque sí varía (por ejemplo, al agregar más unidades). Si intentas cambiar precio después de crearlo, Kotlin da un error de compilación, ya que un val no se puede reasignar.

## Rama-sin-IA:

Carrito de compras usando una data class Producto y funciones como calcularSubtotal, calcularIGV, calcularTotal, mostrarDetalle, calcularDescuento, buscarProducto.

### Resultado de la ejecucion (Rama-sin-IA)

<img width="881" height="718" alt="image" src="https://github.com/user-attachments/assets/99edd282-63d4-4def-b06f-1bcdb3226e07" />


