package com.tecsup.ayudacampus.data.local

import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.local.entity.Comentario
import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.model.Rol

object DatosIniciales {

    suspend fun sembrar(db: AyudaDatabase) {
        if (db.usuarioDao().contar() != 0) return

        val ahora = System.currentTimeMillis()
        val hora = 3600 * 1000L

        // 5 categorias (ids 1 a 5 en este orden)
        db.categoriaDao().insertarTodas(
            listOf(
                Categoria(nombre = "Hardware", icono = "pc"),
                Categoria(nombre = "Software", icono = "code"),
                Categoria(nombre = "Red / Wi-Fi", icono = "wifi"),
                Categoria(nombre = "Cuenta", icono = "key"),
                Categoria(nombre = "Impresión", icono = "printer")
            )
        )

        // 2 usuarios: estudiante (id 1) y soporte (id 2)
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Mijael Lino", correo = "mijael@campus.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Soporte TI", correo = "soporte@campus.test", clave = "admin", rol = Rol.ADMIN.name)
            )
        )

        val dao = db.ticketDao()

        // 6 tickets de ejemplo en distintos estados y prioridades
        val t1 = dao.insertarTicket(
            Ticket(clienteId = 1, categoriaId = 3, titulo = "Sin Wi-Fi en el laboratorio 3",
                descripcion = "Desde ayer no hay señal de Wi-Fi en todo el laboratorio.",
                ubicacion = "Lab 3 · Pabellón B", prioridad = Prioridad.ALTA.name,
                estado = EstadoTicket.EN_PROCESO.name, fecha = ahora - 2 * hora)
        ).toInt()

        dao.insertarTicket(
            Ticket(clienteId = 1, categoriaId = 1, titulo = "Teclado no responde",
                descripcion = "El teclado de la PC 12 no responde a ninguna tecla.",
                ubicacion = "Aula 204", prioridad = Prioridad.MEDIA.name,
                estado = EstadoTicket.ABIERTO.name, fecha = ahora - 24 * hora)
        )

        val t3 = dao.insertarTicket(
            Ticket(clienteId = 1, categoriaId = 5, titulo = "No puedo imprimir en biblioteca",
                descripcion = "La impresora de biblioteca no recibe mis documentos.",
                ubicacion = "Biblioteca", prioridad = Prioridad.BAJA.name,
                estado = EstadoTicket.RESUELTO.name, fecha = ahora - 72 * hora)
        ).toInt()

        dao.insertarTicket(
            Ticket(clienteId = 1, categoriaId = 4, titulo = "Olvidé mi contraseña del aula virtual",
                descripcion = "No puedo entrar al aula virtual, olvidé mi contraseña.",
                ubicacion = "Campus virtual", prioridad = Prioridad.BAJA.name,
                estado = EstadoTicket.ABIERTO.name, fecha = ahora - 26 * hora)
        )

        dao.insertarTicket(
            Ticket(clienteId = 1, categoriaId = 1, titulo = "Servidor del aula 305 caído",
                descripcion = "El servidor del aula 305 no enciende desde la mañana.",
                ubicacion = "Aula 305", prioridad = Prioridad.ALTA.name,
                estado = EstadoTicket.ABIERTO.name, fecha = ahora - 3 * hora)
        )

        dao.insertarTicket(
            Ticket(clienteId = 1, categoriaId = 2, titulo = "Instalar Android Studio",
                descripcion = "Necesito Android Studio instalado en la PC 5 del laboratorio.",
                ubicacion = "Lab 1", prioridad = Prioridad.MEDIA.name,
                estado = EstadoTicket.CERRADO.name, fecha = ahora - 120 * hora,
                fechaCierre = ahora - 96 * hora, calificacion = 5)
        )

        // Comentarios de ejemplo
        dao.insertarComentario(
            Comentario(ticketId = t1, autorId = 2, autorNombre = "Soporte TI", autorRol = Rol.ADMIN.name,
                texto = "Revisaremos el access point del pabellón B hoy.", fecha = ahora - 90 * 60 * 1000L)
        )
        dao.insertarComentario(
            Comentario(ticketId = t1, autorId = 1, autorNombre = "Mijael Lino", autorRol = Rol.CLIENTE.name,
                texto = "Gracias, quedo atento.", fecha = ahora - 80 * 60 * 1000L)
        )
        dao.insertarComentario(
            Comentario(ticketId = t3, autorId = 2, autorNombre = "Soporte TI", autorRol = Rol.ADMIN.name,
                texto = "Reiniciamos el driver de la impresora. Ya debería imprimir.", fecha = ahora - 30 * hora)
        )
    }
}