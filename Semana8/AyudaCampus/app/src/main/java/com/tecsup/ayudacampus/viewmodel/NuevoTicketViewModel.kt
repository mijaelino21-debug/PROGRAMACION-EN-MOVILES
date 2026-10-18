package com.tecsup.ayudacampus.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NuevoTicketViewModel(
    private val repo: AyudaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    // Solo categorias activas
    val categorias: StateFlow<List<Categoria>> = repo.observarCategoriasActivas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Estado del formulario
    var categoriaId by mutableStateOf<Int?>(null); private set
    var titulo by mutableStateOf(""); private set
    var ubicacion by mutableStateOf(""); private set
    var descripcion by mutableStateOf(""); private set
    var prioridad by mutableStateOf(Prioridad.MEDIA); private set

    // Errores por campo
    var errorCategoria by mutableStateOf<String?>(null); private set
    var errorTitulo by mutableStateOf<String?>(null); private set
    var errorDescripcion by mutableStateOf<String?>(null); private set

    var enviado by mutableStateOf(false); private set

    fun cambiarCategoria(id: Int) { categoriaId = id; errorCategoria = null }
    fun cambiarTitulo(t: String) { titulo = t; errorTitulo = null }
    fun cambiarUbicacion(t: String) { ubicacion = t }
    fun cambiarDescripcion(t: String) { descripcion = t; errorDescripcion = null }
    fun cambiarPrioridad(p: Prioridad) { prioridad = p }

    fun enviar() {
        // Reglas: categoria elegida, titulo >= 5, descripcion >= 10
        errorCategoria = if (categoriaId == null) "Elige una categoría" else null
        errorTitulo = if (titulo.trim().length < 5) "Mínimo 5 caracteres" else null
        errorDescripcion = if (descripcion.trim().length < 10) "Mínimo 10 caracteres" else null
        if (errorCategoria != null || errorTitulo != null || errorDescripcion != null) return

        viewModelScope.launch {
            val clienteId = prefs.usuarioId.first()
            repo.crearTicket(clienteId, categoriaId!!, titulo.trim(), descripcion.trim(), ubicacion.trim(), prioridad)
            enviado = true
        }
    }
}