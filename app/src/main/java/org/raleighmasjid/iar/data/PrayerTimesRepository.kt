package org.raleighmasjid.iar.data

import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.api.decodeList
import org.raleighmasjid.iar.api.encodeList
import org.raleighmasjid.iar.model.PrayerDay

class PrayerTimesRepository(private val dataStoreManager: DataStoreManager) {
    suspend fun getCachedPrayerTimes(): List<PrayerDay> {
        val jsonString = dataStoreManager.getCachedPrayerTimesData() ?: return emptyList()
        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(jsonString = jsonString)
        return prayerDays ?: emptyList()
    }

    suspend fun fetchPrayerTimes(): List<PrayerDay> {
        val prayerDays = ApiClient.getPrayerTimes()
        val jsonString = ApiClient.moshi.encodeList(prayerDays)
        dataStoreManager.cachePrayerTimesData(jsonString)
        return prayerDays
    }
}