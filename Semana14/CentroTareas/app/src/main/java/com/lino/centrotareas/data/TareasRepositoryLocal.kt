package com.lino.centrotareas.data

import java.io.IOException
import kotlinx.coroutines.delay

class TareasRepositoryLocal : TareasRepository {
    override suspend fun contarPrimos(hasta: Int): Int {
        // TODO Paso 5
        return 0
    }

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) {
            throw IOException("Sin conexión")
        }
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }
}
