package com.madinaapps.iarmasjid.data

import com.madinaapps.iarmasjid.api.ApiClient
import com.madinaapps.iarmasjid.model.json.PrayerSchedule
import java.util.Date

class PrayerScheduleRepository(private val dataStoreManager: DataStoreManager) {

    suspend fun getCachedPrayerSchedule(): PrayerSchedule? {
        val jsonString = dataStoreManager.getCachedPrayerScheduleData() ?: return null
        return ApiClient.moshi.adapter(PrayerSchedule::class.java).fromJson(jsonString)
    }

    suspend fun fetchPrayerSchedule(forceRefresh: Boolean): Result<PrayerSchedule> {
        val currentCache = getCachedPrayerSchedule()
        if (currentCache != null && currentCache.isValidCache() && !forceRefresh) {
            return Result.success(currentCache)
        }

        try {
            val response = ApiClient.getPrayerSchedule()
            val prayerSchedule = response.body()
            if (!response.isSuccessful || prayerSchedule == null) {
                return Result.failure(Exception("Network Error"))
            }
            prayerSchedule.cacheTimestamp = Date().time
            val jsonString = ApiClient.moshi.adapter(PrayerSchedule::class.java).toJson(prayerSchedule)
            dataStoreManager.cachePrayerScheduleData(jsonString)
            return Result.success(prayerSchedule)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}