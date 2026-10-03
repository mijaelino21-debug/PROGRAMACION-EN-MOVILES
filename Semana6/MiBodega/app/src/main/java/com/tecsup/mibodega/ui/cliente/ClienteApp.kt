package com.tecsup.mibodega.ui.cliente

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.PantallaLogin
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidoReal
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"
    const val CATEGORIAS = "categorias"
    const val PEDIDOS = "pedidos"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

@Composable
fun ClienteApp() {
    val navController = rememberNavController()
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var pedidos by remember { mutableStateOf<List<PedidoReal>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(route = Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onTerminos = { }
            )
        }

        composable(route = Rutas.LOGIN) {
            PantallaLogin(
                onLoginExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                },
                onIrACrearCuenta = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        composable(route = Rutas.REGISTRO) {
            RegistroScreen(
                onCrearCuenta = { _, _, _, _ ->
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(route = Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                onNavegar = { ruta -> navController.navigate(ruta) },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { prod -> navController.navigate(Rutas.detalle(prod.id)) },
                onAgregarProducto = { prod ->
                    val existe = carrito.find { it.producto.id == prod.id }
                    carrito = if (existe != null) {
                        carrito.map { item ->
                            if (item.producto.id == prod.id) item.copy(cantidad = item.cantidad + 1)
                            else item
                        }
                    } else {
                        carrito + ItemCarrito(producto = prod, cantidad = 1)
                    }
                }
            )
        }

        composable(route = Rutas.DETALLE) { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("productoId")?.toIntOrNull()
            val prodSeleccionado = listaProductosFake.find { it.id == idParam }

            if (prodSeleccionado != null) {
                DetalleProductoScreen(
                    producto = prodSeleccionado,
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { prod, cant ->
                        val existe = carrito.find { it.producto.id == prod.id }
                        carrito = if (existe != null) {
                            carrito.map { item ->
                                if (item.producto.id == prod.id) item.copy(cantidad = item.cantidad + cant)
                                else item
                            }
                        } else {
                            carrito + ItemCarrito(producto = prod, cantidad = cant)
                        }
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                onVolver = { navController.popBackStack() },
                onIncrementar = { prod ->
                    carrito = carrito.map { item ->
                        if (item.producto.id == prod.id) item.copy(cantidad = item.cantidad + 1)
                        else item
                    }
                },
                onDecrementar = { prod ->
                    carrito = carrito.mapNotNull { item ->
                        if (item.producto.id == prod.id) {
                            if (item.cantidad > 1) item.copy(cantidad = item.cantidad - 1) else null
                        } else item
                    }
                },
                onEliminar = { prod ->
                    carrito = carrito.filterNot { item -> item.producto.id == prod.id }
                },
                onContinuarPedido = {
                    navController.navigate(Rutas.ENTREGA)
                }
            )
        }

        composable(route = Rutas.ENTREGA) {
            DatosEntregaScreen(
                onVolver = { navController.popBackStack() },
                onConfirmarPedido = { navController.navigate(Rutas.CONFIRMACION) }
            )
        }

        composable(route = Rutas.CATEGORIAS) {
            CategoriasScreen()
        }

        composable(route = Rutas.PEDIDOS) {
            PedidosScreen(pedidos = pedidos)
        }

        composable(route = Rutas.CONFIRMACION) {
            ConfirmacionScreen(
                onVolverInicio = {
                    if (carrito.isNotEmpty()) {
                        val totalCalculado = carrito.sumOf { it.producto.precio * it.cantidad }
                        val nuevoPedido = PedidoReal(
                            id = "#PED-00${pedidos.size + 1}",
                            fecha = "2 Oct 2026",
                            total = "S/ %.2f".format(totalCalculado),
                            estado = "En proceso"
                        )
                        pedidos = pedidos + nuevoPedido
                        carrito = emptyList()
                    }
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            )
        }
    }
}