package org.raleighmasjid.iar.viewModel

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.raleighmasjid.iar.model.Prayer

class AlarmPreferences(appContext: Context) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        fun alarmKey(prayer: Prayer): Preferences.Key<Boolean> {
            return booleanPreferencesKey(prayer.toString())
        }
    }

    private val context: Context = appContext

    suspend fun setAlarm(enabled: Boolean, prayer: Prayer) {
        Log.d("INFO", "set alarm for $prayer")
        context.dataStore.edit { pref ->
            pref[alarmKey(prayer = prayer)] = enabled
        }
    }

    fun getAlarm(prayer: Prayer): Flow<Boolean> {
        return context.dataStore.data.map { pref ->
            pref[alarmKey(prayer = prayer)] ?: false
        }.distinctUntilChanged()
    }
}