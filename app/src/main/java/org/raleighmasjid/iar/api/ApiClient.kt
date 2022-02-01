package org.raleighmasjid.iar.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val baseURL = "https://raleighmasjid.org/API/"
        private val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private val service: RaleighMasjidService = retrofit.create(RaleighMasjidService::class.java)

        suspend fun getPrayerTimes() = service.getPrayerTimes()
    }
}