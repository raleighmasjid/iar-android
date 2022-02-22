package org.raleighmasjid.iar.api

import org.raleighmasjid.iar.model.PrayerSchedule
import retrofit2.Response
import retrofit2.http.GET

interface RaleighMasjidService {
    @GET("prayer-schedule")
    suspend fun getPrayerSchedule(): Response<PrayerSchedule>
}