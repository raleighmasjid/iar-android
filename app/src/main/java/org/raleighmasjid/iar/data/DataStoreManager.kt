package org.raleighmasjid.iar.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.raleighmasjid.iar.model.NotificationType
import org.raleighmasjid.iar.model.Prayer

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")

class DataStoreManager(appContext: Context) {
    companion object {
        val PRAYER_CACHE_KEY = stringPreferencesKey("PRAYER_SCHEDULE_CACHE")
        val NEWS_CACHE_KEY = stringPreferencesKey("NEWS_CACHE")
        val NOTIFICATION_TYPE_KEY = stringPreferencesKey("NOTIFICATION_TYPE")
        val VIEWED_SPECIAL_KEY = intPreferencesKey("VIEWED_SPECIAL")

        fun notificationKey(prayer: Prayer): Preferences.Key<Boolean> {
            return booleanPreferencesKey(prayer.toString())
        }
    }

    private val context: Context = appContext

    suspend fun setViewedSpecial(id: Int) {
        context.dataStore.edit { pref ->
            pref[VIEWED_SPECIAL_KEY] = id
        }
    }

    fun getViewedSpecial(): Flow<Int> {
        return context.dataStore.data.map { pref ->
            pref[VIEWED_SPECIAL_KEY] ?: 0
        }.distinctUntilChanged()
    }

    suspend fun setNotificationType(type: NotificationType) {
        context.dataStore.edit { pref ->
            pref[NOTIFICATION_TYPE_KEY] = type.toString()
        }
    }

    fun getNotificationType(): Flow<NotificationType> {
        return context.dataStore.data.map { pref ->
            pref[NOTIFICATION_TYPE_KEY] ?: ""
        }.map {
            try {
                NotificationType.valueOf(it)
            } catch (e: Exception) {
                NotificationType.SAADALGHAMIDI
            }
        }.distinctUntilChanged()
    }

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

    fun enabledNotifications(): List<Prayer> {
        return Prayer.values().filter { runBlocking { getNotificationEnabled(it).first() } }
    }

    suspend fun getCachedPrayerScheduleData(): String? {
        val data = context.dataStore.data.firstOrNull() ?: return null
        return data[DataStoreManager.PRAYER_CACHE_KEY]
    }

    suspend fun cachePrayerScheduleData(jsonString: String) {
        context.dataStore.edit { pref ->
            pref[DataStoreManager.PRAYER_CACHE_KEY] = jsonString
        }
    }

    suspend fun getCachedNewsData(): String? {
        val data = context.dataStore.data.firstOrNull() ?: return null
        return data[DataStoreManager.NEWS_CACHE_KEY]
    }

    suspend fun cacheNewsData(jsonString: String) {
        context.dataStore.edit { pref ->
            pref[DataStoreManager.NEWS_CACHE_KEY] = jsonString
        }
    }
}
