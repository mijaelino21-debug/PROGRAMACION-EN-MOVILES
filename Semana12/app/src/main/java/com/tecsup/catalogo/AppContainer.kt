package com.tecsup.catalogo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo.data.auth.AuthRepository
import com.tecsup.catalogo.data.auth.FirebaseAuthRepository
import com.tecsup.catalogo.data.local.AppDatabase
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.data.repository.HotelRepositoryImpl
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.notification.NotificadorAndroid
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.auth.AuthViewModel
import com.tecsup.catalogo.ui.lista.ListaViewModel

/** Crea la API, la base de datos y los repositorios en un solo lugar. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val database = AppDatabase.obtenerBaseDatos(appContext)

    val authRepository: AuthRepository = FirebaseAuthRepository()
    val hotelRepository: HotelRepository =
        HotelRepositoryImpl(RetrofitClient.apiService, database.hotelDao())
    val notificador: Notificador = NotificadorAndroid(appContext)
}

/** Los ViewModel se crean con esta factory, que recibe el AppContainer. */
class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) ->
            AuthViewModel(container.authRepository)
        modelClass.isAssignableFrom(ListaViewModel::class.java) ->
            ListaViewModel(container.hotelRepository, container.notificador)
        modelClass.isAssignableFrom(AdminViewModel::class.java) ->
            AdminViewModel(container.hotelRepository, container.notificador)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}