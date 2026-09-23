package com.lino.tecsupfit.repository

import androidx.compose.runtime.mutableStateListOf
import com.lino.tecsupfit.model.ClaseModel
import com.lino.tecsupfit.model.ReservaModel

object ReservasRepository {
    val clasesDisponibles = listOf(
        ClaseModel("1", "Yoga funcional", "7:00 am", "Sala 2", "45 min", "Entrenamiento de movilidad y flexibilidad.", 5, 10, "Hoy"),
        ClaseModel("2", "Cross Training", "6:00 pm", "Sala 1", "50 min", "Entrenamiento funcional de alta intensidad.", 8, 12, "Hoy"),
        ClaseModel("3", "Spinning", "7:30 pm", "Sala 3", "45 min", "Ciclismo de alta intensidad bajo techo.", 3, 15, "Esta semana"),
        ClaseModel("4", "Pilates", "8:00 am", "Sala 2", "50 min", "Fortalecimiento de core y postura.", 6, 10, "Esta semana")
    )

    val misReservas = mutableStateListOf(
        ReservaModel("101", "Cross Training", "Hoy, 6:00 pm", "Sala 1", "Confirmada"),
        ReservaModel("102", "Yoga funcional", "Ayer, 7:00 am", "Sala 2", "Completada")
    )

    fun agregarReserva(clase: ClaseModel, horarioSeleccionado: String) {
        val nuevaReserva = ReservaModel(
            id = System.currentTimeMillis().toString(),
            nombreClase = clase.nombre,
            horario = "$horarioSeleccionado, ${clase.sala}",
            sala = clase.sala,
            estado = "Confirmada"
        )
        misReservas.add(0, nuevaReserva)
    }

    fun cancelarReserva(idReserva: String) {
        misReservas.removeIf { it.id == idReserva }
    }

    fun marcarComoCompletada(idReserva: String) {
        val index = misReservas.indexOfFirst { it.id == idReserva }
        if (index != -1) {
            val reserva = misReservas[index]
            misReservas[index] = reserva.copy(estado = "Completada")
        }
    }
}