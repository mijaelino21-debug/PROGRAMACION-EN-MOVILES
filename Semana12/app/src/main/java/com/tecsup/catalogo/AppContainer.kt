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
import com.tecsup.catalogo.data.repository.ReservaRepository
import com.tecsup.catalogo.data.repository.ReservaRepositoryImpl
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.notification.NotificadorAndroid
import com.tecsup.catalogo.ui.admin.AdminHotelesViewModel
import com.tecsup.catalogo.ui.admin.AdminReservasViewModel
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.auth.AuthViewModel
import com.tecsup.catalogo.ui.lista.ListaViewModel
import com.tecsup.catalogo.ui.reservas.MisReservasViewModel

/** Crea la API, la base de datos y los repositorios en un solo lugar. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val database = AppDatabase.obtenerBaseDatos(appContext)

    val authRepository: AuthRepository = FirebaseAuthRepository()
    val hotelRepository: HotelRepository =
        HotelRepositoryImpl(RetrofitClient.apiService, database.hotelDao())
    val reservaRepository: ReservaRepository = ReservaRepositoryImpl(database.reservaDao())
    val notificador: Notificador = NotificadorAndroid(appContext)
}

/** Los ViewModel se crean con esta factory, que recibe el AppContainer. */
class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) ->
            AuthViewModel(container.authRepository) as T
        modelClass.isAssignableFrom(ListaViewModel::class.java) ->
            ListaViewModel(container.hotelRepository, container.notificador) as T
        modelClass.isAssignableFrom(AdminViewModel::class.java) ->
            AdminViewModel(container.hotelRepository, container.reservaRepository, container.notificador) as T
        modelClass.isAssignableFrom(AdminHotelesViewModel::class.java) ->
            AdminHotelesViewModel(container.hotelRepository) as T
        modelClass.isAssignableFrom(MisReservasViewModel::class.java) ->
            MisReservasViewModel(container.reservaRepository) as T
        modelClass.isAssignableFrom(AdminReservasViewModel::class.java) ->
            AdminReservasViewModel(container.reservaRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
