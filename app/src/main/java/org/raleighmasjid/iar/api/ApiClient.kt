package org.raleighmasjid.iar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class ApiClient {
    companion object {
        private const val baseURL = "https://raleighmasjid.org/API/"
        private val moshi: Moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
        private val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        private val service: RaleighMasjidService = retrofit.create(RaleighMasjidService::class.java)

        suspend fun getPrayerTimes() = service.getPrayerTimes()
    }
}