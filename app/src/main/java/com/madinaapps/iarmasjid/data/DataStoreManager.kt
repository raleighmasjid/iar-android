package com.madinaapps.iarmasjid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")

class DataStoreManager(appContext: Context) {
    companion object {
        val PRAYER_CACHE_KEY = stringPreferencesKey("PRAYER_SCHEDULE_CACHE_B1")
        val NEWS_CACHE_KEY = stringPreferencesKey("NEWS_CACHE_B2")
        val NOTIFICATION_TYPE_KEY = stringPreferencesKey("NOTIFICATION_TYPE_B1")
        val VIEWED_ANNOUNCEMENTS_KEY = stringSetPreferencesKey("VIEWED_ANNOUNCEMENTS_B1")

        fun notificationKey(prayer: Prayer): Preferences.Key<Boolean> {
            return booleanPreferencesKey(prayer.toString())
        }
    }

    private val context: Context = appContext

    suspend fun setViewedAnnouncments(ids: Set<String>) {
        context.dataStore.edit { pref ->
            pref[VIEWED_ANNOUNCEMENTS_KEY] = ids
        }
    }

    fun getViewedAnnouncments(): Flow<Set<String>> {
        return context.dataStore.data.map { pref ->
            pref[VIEWED_ANNOUNCEMENTS_KEY] ?: emptySet()
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
