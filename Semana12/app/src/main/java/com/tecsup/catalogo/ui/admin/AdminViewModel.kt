package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminEstadisticas(
    val totalGuardado: Int = 0,
    val totalFavoritos: Int = 0
)

class AdminViewModel(
    private val repository: HotelRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estadisticas = MutableStateFlow(AdminEstadisticas())
    val estadisticas: StateFlow<AdminEstadisticas> = _estadisticas.asStateFlow()

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargarEstadisticas() {
        viewModelScope.launch {
            val total = repository.cantidadGuardada()
            val favoritos = repository.cantidadFavoritos()
            _estadisticas.value = AdminEstadisticas(total, favoritos)
        }
    }

    fun sincronizar() {
        if (_sincronizando.value) return
        viewModelScope.launch {
            _sincronizando.value = true
            repository.refrescar()
                .onSuccess { cantidad ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $cantidad elementos")
                    _mensaje.value = "Sincronizado: $cantidad elementos"
                }
                .onFailure { error ->
                    _mensaje.value = mensajeDe(error)
                }
            _sincronizando.value = false
            cargarEstadisticas()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repository.limpiarCache()
            _mensaje.value = "Caché limpiada"
            cargarEstadisticas()
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}