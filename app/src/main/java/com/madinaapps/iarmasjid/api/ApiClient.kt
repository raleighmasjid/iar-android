package com.madinaapps.iarmasjid.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date

inline fun <reified T> Moshi.decodeList(jsonString: String): List<T>? {
    return adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java)).fromJson(jsonString)
}

class ApiClient {
    companion object {
        private const val BASE_URL = "https://raleighmasjid.org/API/app/"
        val moshi: Moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        private val service: RaleighMasjidService = retrofit.create(RaleighMasjidService::class.java)

        suspend fun getPrayerSchedule() = service.getPrayerSchedule()

        suspend fun getNews() = service.getNews()
    }
}