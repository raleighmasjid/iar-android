package com.madinaapps.iarmasjid.api

import androidx.annotation.Keep
import com.madinaapps.iarmasjid.model.json.News
import com.madinaapps.iarmasjid.model.json.PrayerSchedule
import retrofit2.Response
import retrofit2.http.GET

interface RaleighMasjidService {
    @Keep
    @GET("prayer-schedule")
    suspend fun getPrayerSchedule(): Response<PrayerSchedule>

    @Keep
    @GET("news")
    suspend fun getNews(): Response<News>
}