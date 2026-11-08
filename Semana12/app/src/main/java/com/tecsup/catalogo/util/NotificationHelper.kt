package com.tecsup.catalogo.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val channelId = "hotel_sync_channel"
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        crearCanalNotificacion()
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sincronización de Hoteles",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifica cuando los hoteles han sido sincronizados"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun mostrarNotificacionSincronizacion(cantidad: Int) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle("Sincronización Completada")
            .setContentText("Se han actualizado $cantidad hoteles exitosamente.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}