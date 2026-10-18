package com.tecsup.ayudacampus.viewmodel

import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.model.EstadoTicket

object SlaUtils {

    /**
     * Calcula el tiempo transcurrido desde la creacion del ticket.
     */
    fun calcularHace(fecha: Long, ahora: Long = System.currentTimeMillis()): String {
        val min = (ahora - fecha) / 60_000
        return when {
            min < 60 -> "Hace $min min"
            min < 60 * 24 -> "Hace ${min / 60} h"
            else -> "Hace ${min / (60 * 24)} d"
        }
    }

    /**
     * Calcula el tiempo de SLA restante para un ticket segun su prioridad:
     * - Alta: 4 horas
     * - Media: 24 horas
     * - Baja: 72 horas
     */
    fun calcularSla(ticket: Ticket, ahora: Long = System.currentTimeMillis()): String {
        val limiteHoras = when (ticket.prioridad.uppercase()) {
            "ALTA" -> 4L
            "MEDIA" -> 24L
            "BAJA" -> 72L
            else -> 24L
        }
        val limiteMs = limiteHoras * 60 * 60 * 1000L
        val fechaLimite = ticket.fecha + limiteMs

        val esFinalizado = ticket.estado == EstadoTicket.RESUELTO.name || ticket.estado == EstadoTicket.CERRADO.name
        val tiempoRef = if (esFinalizado && ticket.fechaCierre != null) ticket.fechaCierre else ahora

        val diffMs = fechaLimite - tiempoRef

        if (esFinalizado) {
            return if (diffMs >= 0) "SLA Cumplido" else "SLA Vencido"
        }

        if (diffMs <= 0) {
            val atrasoMin = (-diffMs) / 60_000L
            val horas = atrasoMin / 60
            val min = atrasoMin % 60
            return if (horas > 0) "SLA Vencido (-${horas}h ${min}m)" else "SLA Vencido (-${min}m)"
        }

        val minRestantes = diffMs / 60_000L
        val horasRestantes = minRestantes / 60
        val diasRestantes = horasRestantes / 24

        return when {
            diasRestantes > 0 -> "SLA: ${diasRestantes}d ${horasRestantes % 24}h restantes"
            horasRestantes > 0 -> "SLA: ${horasRestantes}h ${minRestantes % 60}m restantes"
            else -> "SLA: ${minRestantes}m restantes"
        }
    }
}
