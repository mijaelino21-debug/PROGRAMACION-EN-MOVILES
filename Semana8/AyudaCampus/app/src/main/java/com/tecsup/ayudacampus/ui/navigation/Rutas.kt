package com.tecsup.ayudacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    // Estudiante
    const val MIS_TICKETS = "mis_tickets"
    const val NUEVO_TICKET = "nuevo_ticket"
    const val DETALLE_TICKET = "detalle_ticket/{ticketId}"
    const val PERFIL = "perfil"

    // Soporte TI
    const val PANEL = "panel"
    const val BANDEJA = "bandeja"
    const val ATENDER_TICKET = "atender_ticket/{ticketId}"
    const val CATEGORIAS = "categorias"
    const val FORMULARIO_CATEGORIA = "formulario_categoria/{categoriaId}"
    const val AJUSTES = "ajustes"

    fun detalleTicket(id: Int) = "detalle_ticket/$id"
    fun atenderTicket(id: Int) = "atender_ticket/$id"
    fun formularioCategoria(id: Int = 0) = "formulario_categoria/$id"
}