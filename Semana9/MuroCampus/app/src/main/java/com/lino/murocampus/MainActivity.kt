package com.lino.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lino.murocampus.ui.PantallaCrear
import com.lino.murocampus.ui.PantallaDetalle
import com.lino.murocampus.ui.PantallaLista
import com.lino.murocampus.ui.theme.MuroCampusTheme
import com.lino.murocampus.viewmodel.PostsViewModel
import com.lino.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
            var creando by rememberSaveable { mutableStateOf(false) }
            BackHandler(enabled = creando || idSeleccionado != null) {
                if (creando) creando = false else idSeleccionado = null
            }
            val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory())
            MuroCampusTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { MuroTopBar() },
                    floatingActionButton = {
                        if (!creando && idSeleccionado == null) {
                            FloatingActionButton(onClick = {
                                vm.limpiarEnvio()
                                creando = true
                            }) {
                                Text("+")
                            }
                        }
                    }
                ) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)
                    val seleccionado = idSeleccionado
                    when {
                        creando -> PantallaCrear(vm = vm, modifier = modifier)
                        seleccionado == null -> PantallaLista(
                            vm = vm,
                            modifier = modifier,
                            onPostClick = { id ->
                                vm.abrir(id)
                                idSeleccionado = id
                            }
                        )
                        else -> PantallaDetalle(vm = vm, id = seleccionado, modifier = modifier)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroTopBar() {
    TopAppBar(
        title = { Text("MuroCampus") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
