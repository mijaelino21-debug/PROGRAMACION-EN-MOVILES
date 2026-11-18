package com.lino.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lino.centrotareas.data.TareasRepository
import com.lino.centrotareas.model.CargaUi
import com.lino.centrotareas.model.EstadisticaModo
import com.lino.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private var trabajo: Job? = null
    private var trabajoPrimos: Job? = null

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    private val _estadisticasTiempos = MutableStateFlow(
        listOf(
            EstadisticaModo("Una por una"),
            EstadisticaModo("A la vez")
        )
    )
    val estadisticasTiempos: StateFlow<List<EstadisticaModo>> = _estadisticasTiempos.asStateFlow()

    val historial: StateFlow<List<String>> = repo.obtenerHistorial()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun registrarTiempo(modalidad: String, milisegundos: Long) {
        _estadisticasTiempos.value = _estadisticasTiempos.value.map { est ->
            if (est.modalidad == modalidad) {
                est.copy(
                    cantidadEjecuciones = est.cantidadEjecuciones + 1,
                    tiempoTotalMs = est.tiempoTotalMs + milisegundos,
                    ultimoTiempoMs = milisegundos
                )
            } else {
                est
            }
        }
    }

    fun tareaLarga() {
        if (_corriendo.value) return

        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                for (i in 1..100) {
                    withContext(Dispatchers.Default) {
                        if (i == 1) Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                        delay(40)
                        _progreso.value = i / 100f
                    }
                }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    fun contar(hasta: Int) {
        if (_primos.value is PrimosUi.Calculando) return

        _primos.value = PrimosUi.Calculando(0f)
        trabajoPrimos = viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                var cantidadFinal = 0
                repo.contarPrimos(hasta).collect { progresoPrimos ->
                    _primos.value = PrimosUi.Calculando(progresoPrimos.progreso)
                    cantidadFinal = progresoPrimos.cantidad
                }
                val milisegundos = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidadFinal, milisegundos)
                repo.guardarResultado("Primos (hasta $hasta): $cantidadFinal primos ($milisegundos ms)")
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando
        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(_simularError.value)
                val milisegundos = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito(
                    "Una por una",
                    perfil,
                    pedidos.size,
                    milisegundos
                )
                registrarTiempo("Una por una", milisegundos)
                repo.guardarResultado("Carga (Una por una): ${pedidos.size} pedidos ($milisegundos ms)")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando
        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                coroutineScope {
                    val a = async { repo.cargarPerfil() }
                    val b = async { repo.cargarPedidos(_simularError.value) }
                    val perfil = a.await()
                    val pedidos = b.await()
                    val milisegundos = System.currentTimeMillis() - inicio
                    _carga.value = CargaUi.Exito(
                        "A la vez",
                        perfil,
                        pedidos.size,
                        milisegundos
                    )
                    registrarTiempo("A la vez", milisegundos)
                    repo.guardarResultado("Carga (A la vez): ${pedidos.size} pedidos ($milisegundos ms)")
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }
}
