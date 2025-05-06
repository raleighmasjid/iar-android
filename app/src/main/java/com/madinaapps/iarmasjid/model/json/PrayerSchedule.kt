package com.madinaapps.iarmasjid.model.json

import androidx.annotation.Keep
import com.madinaapps.iarmasjid.utils.asLocalDate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@Keep
@JsonClass(generateAdapter = true)
data class PrayerSchedule(
    @Json(name = "prayer_days")
    val prayerDays: List<PrayerDay>,

    @Json(name = "friday_schedule")
    val fridaySchedule: List<FridayPrayer>,

    @Json(name = "cache_timestamp")
    var cacheTimestamp: Long?
 ) {
    fun validDays(): List<PrayerDay> {
        val today = Date().asLocalDate()
        return prayerDays.filter { it.date.asLocalDate() >= today }
    }

    fun isValidCache(): Boolean {
        val cacheTime = cacheTimestamp ?: return false
        return Date().time - cacheTime < (5 * 60 * 1000)
    }
}