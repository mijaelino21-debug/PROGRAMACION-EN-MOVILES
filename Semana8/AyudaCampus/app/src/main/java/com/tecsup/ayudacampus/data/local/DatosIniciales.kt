package com.tecsup.ayudacampus.data.local

import com.tecsup.ayudacampus.data.local.entity.Categoria

object DatosIniciales {
    suspend fun sembrar(db: AyudaDatabase) {
        // TODO (Paso 3): cambiar a usuarioDao().contar() == 0 y agregar
        // 2 usuarios, 6 tickets y 3 o 4 comentarios
        if (db.categoriaDao().contar() == 0) {
            db.categoriaDao().insertarTodas(
                listOf(
                    Categoria(nombre = "Hardware", icono = "pc"),
                    Categoria(nombre = "Software", icono = "code"),
                    Categoria(nombre = "Red / Wi-Fi", icono = "wifi"),
                    Categoria(nombre = "Cuenta", icono = "key"),
                    Categoria(nombre = "Impresión", icono = "printer")
                )
            )
        }
    }
}