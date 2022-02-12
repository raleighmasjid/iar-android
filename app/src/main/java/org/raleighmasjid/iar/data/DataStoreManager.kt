package org.raleighmasjid.iar.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.raleighmasjid.iar.model.Prayer

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")

class DataStoreManager(appContext: Context) {
    companion object {
        val CACHE_KEY = stringPreferencesKey("PRAYER_TIMES_CACHE")

        fun notificationKey(prayer: Prayer): Preferences.Key<Boolean> {
            return booleanPreferencesKey(prayer.toString())
        }
    }

    private val context: Context = appContext

    suspend fun setNotification(enabled: Boolean, prayer: Prayer) {
        Log.d("INFO", "set alarm for $prayer")
        context.dataStore.edit { pref ->
            pref[notificationKey(prayer = prayer)] = enabled
        }
    }

    fun getNotificationEnabled(prayer: Prayer): Flow<Boolean> {
        return context.dataStore.data.map { pref ->
            pref[notificationKey(prayer = prayer)] ?: false
        }.distinctUntilChanged()
    }

    suspend fun getCachedPrayerTimesData(): String? {
        val data = context.dataStore.data.firstOrNull() ?: return null
        return data[DataStoreManager.CACHE_KEY]
    }

    suspend fun cachePrayerTimesData(jsonString: String) {
        context.dataStore.edit { pref ->
            pref[DataStoreManager.CACHE_KEY] = jsonString
        }
    }
}
