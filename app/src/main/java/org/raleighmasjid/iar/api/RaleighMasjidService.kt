package org.raleighmasjid.iar.api

import org.raleighmasjid.iar.model.PrayerDay
import retrofit2.http.GET

interface RaleighMasjidService {
    @GET("prayer/app")
    suspend fun getPrayerTimes(): List<PrayerDay>
}