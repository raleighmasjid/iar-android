package org.raleighmasjid.iar.api

import org.raleighmasjid.iar.model.FridaySchedule
import org.raleighmasjid.iar.model.PrayerDay
import retrofit2.Response
import retrofit2.http.GET

interface RaleighMasjidService {
    @GET("prayer")
    suspend fun getPrayerTimes(): Response<List<PrayerDay>>

    @GET("friday-schedule")
    suspend fun getFridaySchedule(): Response<List<FridaySchedule>>
}