package com.tecsup.catalogo.notification

import android.content.Context
import com.tecsup.catalogo.util.NotificationHelper

class NotificadorAndroid(context: Context) : Notificador {
    private val helper = NotificationHelper(context.applicationContext)

    override fun mostrar(titulo: String, texto: String) {
        helper.mostrar(titulo, texto)
    }
}