package com.tecsup.ayudacampus.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {

    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var error by mutableStateOf<String?>(null); private set
    var listo by mutableStateOf(false); private set

    fun alternar(c: Categoria) {
        viewModelScope.launch { repo.cambiarActiva(c.id, !c.activa) }
    }

    // Nueva id  o editada (id existente)
    fun guardar(c: Categoria) {
        if (c.nombre.isBlank()) {
            error = "El nombre es obligatorio"
            return
        }
        viewModelScope.launch {
            repo.guardarCategoria(c.copy(nombre = c.nombre.trim()))
            listo = true
        }
    }

    fun eliminar(c: Categoria) {
        viewModelScope.launch {
            // Room no deja borrar una categoria con tickets (ForeignKey)
            try {
                repo.eliminarCategoria(c)
                listo = true
            } catch (e: Exception) {
                error = "No se puede eliminar: la categoría ya tiene tickets"
            }
        }
    }
}