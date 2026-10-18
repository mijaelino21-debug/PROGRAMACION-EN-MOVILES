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
        modelClass.isAssignableFrom(MisTicketsViewModel::class.java) -> MisTicketsViewModel(repo, prefs) as T
        modelClass.isAssignableFrom(NuevoTicketViewModel::class.java) -> NuevoTicketViewModel(repo, prefs) as T
        modelClass.isAssignableFrom(TicketDetalleViewModel::class.java) -> TicketDetalleViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}