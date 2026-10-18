package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.ui.components.graficos.DatoGrafico
import com.tecsup.ayudacampus.ui.theme.ColorAbierto
import com.tecsup.ayudacampus.ui.theme.ColorCerrado
import com.tecsup.ayudacampus.ui.theme.ColorEnProceso
import com.tecsup.ayudacampus.ui.theme.ColorResuelto
import com.tecsup.ayudacampus.ui.theme.VioletaPrimario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PanelViewModel(repo: AyudaRepository) : ViewModel() {

    val abiertos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoTicket.ABIERTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoTicket.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val resueltos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoTicket.RESUELTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    private val cerrados = repo.observarCantidadPorEstado(EstadoTicket.CERRADO)

    // Conteos de Room convertidos al formato de los graficos
    val porEstado: StateFlow<List<DatoGrafico>> =
        combine(abiertos, enProceso, resueltos, cerrados) { a, p, r, c ->
            listOf(
                DatoGrafico("Abierto", a, ColorAbierto),
                DatoGrafico("En proceso", p, ColorEnProceso),
                DatoGrafico("Resuelto", r, ColorResuelto),
                DatoGrafico("Cerrado", c, ColorCerrado)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porCategoria: StateFlow<List<DatoGrafico>> = repo.observarCantidadPorCategoria()
        .map { lista -> lista.map { DatoGrafico(it.nombre, it.total, VioletaPrimario) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ALTA y ABIERTO con mas de 1 hora (limite = ahora - 1 hora)
    val altasSinAtender: StateFlow<Int> =
        repo.observarAltasSinAtender(System.currentTimeMillis() - 60 * 60 * 1000L)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val tiempoPromedioResolucion: StateFlow<String> = repo.observarTiempoPromedioResolucion()
        .map { ms -> formatearTiempo(ms) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "N/A")

    private fun formatearTiempo(ms: Double?): String {
        if (ms == null || ms <= 0) return "N/A"
        val totalMin = (ms / 60_000).toLong()
        val horas = totalMin / 60
        val minRestantes = totalMin % 60
        val dias = horas / 24
        val horasRestantes = horas % 24

        return when {
            dias > 0 -> if (horasRestantes > 0) "${dias} d ${horasRestantes} h" else "${dias} d"
            horas > 0 -> if (minRestantes > 0) "${horas} h ${minRestantes} min" else "${horas} h"
            totalMin > 0 -> "$totalMin min"
            else -> "< 1 min"
        }
    }
}