package com.lino.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.lino.centrotareas.data.TareasRepository
import com.lino.centrotareas.model.CargaUi
import com.lino.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    fun tareaLargaMala() {
        _mensaje.value = "Trabajando..."
        Thread.sleep(4000)
        _mensaje.value = "Terminó"
    }

    // TODO Paso 2
    // TODO Paso 3
    // TODO Paso 4
    // TODO Paso 5
    // TODO Paso 6
    // TODO Paso 7
}
