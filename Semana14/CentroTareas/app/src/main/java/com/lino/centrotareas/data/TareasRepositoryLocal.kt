package com.lino.centrotareas.data

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TareasRepositoryLocal : TareasRepository {
    override fun contarPrimos(hasta: Int): Flow<ProgresoPrimos> = flow {
        var cantidad = 0
        var ultimoPorcentaje = -1

        for (n in 2..hasta) {
            currentCoroutineContext().ensureActive()
            if (esPrimo(n)) cantidad++

            val porcentaje = ((n.toLong() * 100) / hasta).toInt()
            if (porcentaje > ultimoPorcentaje) {
                ultimoPorcentaje = porcentaje
                emit(ProgresoPrimos(porcentaje / 100f, cantidad))
            }
        }
        if (ultimoPorcentaje < 100) {
            emit(ProgresoPrimos(1.0f, cantidad))
        }
    }.flowOn(Dispatchers.Default)

    private fun esPrimo(n: Int): Boolean {
        var d = 2
        while (d.toLong() * d <= n) {
            if (n % d == 0) return false
            d++
        }
        return true
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
