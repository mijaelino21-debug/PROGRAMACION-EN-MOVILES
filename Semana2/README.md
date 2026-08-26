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


## Rama-con-IA:

Esta rama ("con IA") toma el mismo carrito de compras del laboratorio, pero reescribe su diseño usando Programación Orientada a Objetos con los 4 pilares, en vez de la versión original basada en funciones sueltas y una data class simple. 

<img width="862" height="777" alt="image" src="https://github.com/user-attachments/assets/fd04ff9a-9848-42d0-b7ea-0a1b28bcb677" />
<img width="837" height="626" alt="image" src="https://github.com/user-attachments/assets/7e3cc521-3211-45ac-b69a-b35c949eb7b9" />

## PROMPT USADO :
Ayudame a Rediseñar este carrito de compras en Kotlin bajo un enfoque orientado a objetos, aplicando los cuatro pilares de POO: una clase abstracta Producto como base (abstracción), subclases especializadas que hereden de ella con comportamiento propio (herencia y polimorfismo), y una clase Carrito que encapsule la lista de productos exponiendo solo una API pública controlada.  Incorpora además las funcionalidades de búsqueda y eliminación de productos dentro del carrito.

