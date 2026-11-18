package com.lino.centrotareas.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "historial_tareas_prefs")

class TareasRepositoryLocal(private val context: Context) : TareasRepository {

    private companion object {
        val HISTORIAL_KEY = stringPreferencesKey("historial_resultados")
        const val DELIMITER = "|||"
    }

    override fun obtenerHistorial(): Flow<List<String>> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val actualStr = preferences[HISTORIAL_KEY] ?: ""
                if (actualStr.isEmpty()) {
                    emptyList()
                } else {
                    actualStr.split(DELIMITER)
                }
            }
    }

    override suspend fun guardarResultado(resultado: String) {
        try {
            context.dataStore.edit { preferences ->
                val actualStr = preferences[HISTORIAL_KEY] ?: ""
                val listaActual = if (actualStr.isEmpty()) emptyList() else actualStr.split(DELIMITER)
                val nuevaLista = (listOf(resultado) + listaActual).take(5)
                preferences[HISTORIAL_KEY] = nuevaLista.joinToString(DELIMITER)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("TareasRepositoryLocal", "Error al guardar resultado en DataStore", e)
        }
    }

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
