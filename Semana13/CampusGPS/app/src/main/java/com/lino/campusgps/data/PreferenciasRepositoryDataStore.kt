package com.lino.campusgps.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenciasRepositoryDataStore(
    private val dataStore: DataStore<Preferences>
) : PreferenciasRepository {

    companion object {
        private val CLAVE_ULTIMO_LUGAR = intPreferencesKey("ultimo_lugar_id")
    }

    override val ultimoLugarId: Flow<Int?> = dataStore.data
        .map { preferencias -> preferencias[CLAVE_ULTIMO_LUGAR] }

    override suspend fun guardarUltimoLugar(id: Int) {
        dataStore.edit { preferencias -> preferencias[CLAVE_ULTIMO_LUGAR] = id }
    }
}
