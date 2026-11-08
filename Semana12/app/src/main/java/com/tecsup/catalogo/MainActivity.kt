package com.tecsup.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo.data.local.AppDatabase
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.HotelRepository
import com.tecsup.catalogo.data.repository.HotelRepositoryImpl
import com.tecsup.catalogo.ui.admin.AdminScreen
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.auth.AuthViewModel
import com.tecsup.catalogo.ui.auth.LoginScreen
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val adminViewModel: AdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val database = AppDatabase.obtenerBaseDatos(context)
        val apiService = RetrofitClient.apiService
        val repository: HotelRepositoryImpl = HotelRepositoryImpl(apiService, database.hotelDao())

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListaViewModel(application, repository) as T
            }
        }

        val listaViewModel = ViewModelProvider(this, factory)[ListaViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember {
                        mutableStateOf(authViewModel.isUserLoggedIn)
                    }
                    var verAdminDirecto by remember { mutableStateOf(false) }

                    if (isLoggedIn) {
                        if (adminViewModel.isAdmin && verAdminDirecto) {
                            AdminScreen(
                                viewModel = adminViewModel,
                                onVolverAlCatalogo = { verAdminDirecto = false },
                                onCerrarSesion = {
                                    authViewModel.logout()
                                    verAdminDirecto = false
                                    isLoggedIn = false
                                }
                            )
                        } else {
                            ListaScreen(
                                viewModel = listaViewModel,
                                userEmail = adminViewModel.userEmail,
                                isAdmin = adminViewModel.isAdmin,
                                onIrAlAdmin = { verAdminDirecto = true },
                                onCerrarSesion = {
                                    authViewModel.logout()
                                    isLoggedIn = false
                                }
                            )
                        }
                    } else {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                isLoggedIn = true
                            }
                        )
                    }
                }
            }
        }
    }
}