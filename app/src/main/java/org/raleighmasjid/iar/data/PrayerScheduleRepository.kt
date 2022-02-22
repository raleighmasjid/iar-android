package org.raleighmasjid.iar.data

import android.util.Log
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.model.PrayerSchedule

class PrayerScheduleRepository(private val dataStoreManager: DataStoreManager) {

    suspend fun getCachedPrayerSchedule(): PrayerSchedule? {
        val jsonString = dataStoreManager.getCachedPrayerScheduleData() ?: return null
        return ApiClient.moshi.adapter(PrayerSchedule::class.java).fromJson(jsonString)
    }

    suspend fun fetchPrayerSchedule(): Result<PrayerSchedule> {
        try {
            val response = ApiClient.getPrayerSchedule()
            val prayerSchedule = response.body()
            if (!response.isSuccessful || prayerSchedule == null) {
                return return Result.failure(Exception("Network Error"))
            }
            val jsonString = ApiClient.moshi.adapter(PrayerSchedule::class.java).toJson(prayerSchedule)
            dataStoreManager.cachePrayerScheduleData(jsonString)
            Log.d("INFO", "fetching prayer times")
            return Result.success(prayerSchedule)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}