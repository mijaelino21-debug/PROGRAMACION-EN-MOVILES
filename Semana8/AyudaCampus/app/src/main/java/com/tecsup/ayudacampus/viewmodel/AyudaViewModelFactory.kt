package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository

class AyudaViewModelFactory(
    private val repo: AyudaRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(CategoriasViewModel::class.java) -> CategoriasViewModel(repo) as T
        modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(repo, prefs) as T
        // TODO: agrega aqui cada ViewModel nuevo (pasos 7 al 12)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}