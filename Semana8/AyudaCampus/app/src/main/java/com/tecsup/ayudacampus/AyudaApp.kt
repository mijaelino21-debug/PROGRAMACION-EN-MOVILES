package com.tecsup.ayudacampus

import android.app.Application
import com.tecsup.ayudacampus.data.local.AyudaDatabase
import com.tecsup.ayudacampus.data.local.DatosIniciales
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.repository.AyudaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AyudaApp : Application() {

    lateinit var repository: AyudaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AyudaDatabase.obtener(this)
        repository = AyudaRepositoryRoom(db.categoriaDao())

        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}