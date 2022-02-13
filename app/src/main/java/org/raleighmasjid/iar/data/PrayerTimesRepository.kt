package org.raleighmasjid.iar.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.api.decodeList
import org.raleighmasjid.iar.api.encodeList
import org.raleighmasjid.iar.model.PrayerDay

class PrayerTimesRepository(private val dataStoreManager: DataStoreManager) {

    private val sharedFlow = MutableSharedFlow<Result<List<PrayerDay>>>()
    val updates = sharedFlow.asSharedFlow()

    private suspend fun getCachedPrayerTimes(): List<PrayerDay> {
        val jsonString = dataStoreManager.getCachedPrayerTimesData() ?: return emptyList()
        val prayerDays = ApiClient.moshi.decodeList<PrayerDay>(jsonString = jsonString)
        return prayerDays ?: emptyList()
    }

    suspend fun fetchPrayerTimes() {
        val cache = getCachedPrayerTimes()
        if (cache.isNotEmpty()) {
            sharedFlow.emit(Result.success(cache))
        }

        try {
            val response = ApiClient.getPrayerTimes()
            val prayerDays = response.body()
            if (!response.isSuccessful || prayerDays == null) {
                sharedFlow.emit(Result.failure(Exception("Network Error")))
                return
            }
            val jsonString = ApiClient.moshi.encodeList(prayerDays)
            dataStoreManager.cachePrayerTimesData(jsonString)
            sharedFlow.emit(Result.success(prayerDays))
        } catch (e: Exception) {
            sharedFlow.emit(Result.failure(e))
        }
    }

}