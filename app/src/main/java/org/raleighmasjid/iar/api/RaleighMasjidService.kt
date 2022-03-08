package org.raleighmasjid.iar.api

import org.raleighmasjid.iar.model.json.News
import org.raleighmasjid.iar.model.json.PrayerSchedule
import retrofit2.Response
import retrofit2.http.GET

interface RaleighMasjidService {
    @GET("prayer-schedule")
    suspend fun getPrayerSchedule(): Response<PrayerSchedule>

    @GET("news")
    suspend fun getNews(): Response<News>
}