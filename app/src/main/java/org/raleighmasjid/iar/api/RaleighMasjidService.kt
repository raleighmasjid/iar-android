package org.raleighmasjid.iar.api

import org.raleighmasjid.iar.model.FridaySchedule
import org.raleighmasjid.iar.model.PrayerDay
import retrofit2.http.GET

interface RaleighMasjidService {
    @GET("prayer")
    suspend fun getPrayerTimes(): List<PrayerDay>

    @GET("friday-schedule")
    suspend fun getFridaySchedule(): List<FridaySchedule>
}