package com.lino.campusgps

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lino.campusgps.data.LugaresRepositoryLocal
import com.lino.campusgps.data.PreferenciasRepositoryDataStore
import com.lino.campusgps.data.UbicacionRepositoryGoogle
import com.lino.campusgps.ui.PantallaMapa
import com.lino.campusgps.ui.theme.CampusGPSTheme
import com.lino.campusgps.viewmodel.CampusViewModelFactory
import com.lino.campusgps.viewmodel.MapaViewModel

private val Context.preferenciasDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "campusgps_preferencias"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(
                    factory = CampusViewModelFactory(
                        UbicacionRepositoryGoogle(applicationContext),
                        LugaresRepositoryLocal(),
                        PreferenciasRepositoryDataStore(
                            applicationContext.preferenciasDataStore
                        )
                    )
                )
                PantallaMapa(vm)
            }
        }
    }
}
