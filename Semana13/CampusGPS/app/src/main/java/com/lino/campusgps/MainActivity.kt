package com.lino.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lino.campusgps.data.LugaresRepositoryLocal
import com.lino.campusgps.data.UbicacionRepositoryGoogle
import com.lino.campusgps.ui.PantallaMapa
import com.lino.campusgps.ui.theme.CampusGPSTheme
import com.lino.campusgps.viewmodel.CampusViewModelFactory
import com.lino.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(
                    factory = CampusViewModelFactory(
                        UbicacionRepositoryGoogle(applicationContext),
                        LugaresRepositoryLocal()
                    )
                )
                PantallaMapa(vm)
            }
        }
    }
}
