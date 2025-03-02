package com.madinaapps.iarmasjid.model.json

import com.madinaapps.iarmasjid.utils.asLocalDate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class PrayerSchedule(
    @Json(name = "prayer_days")
    val prayerDays: List<PrayerDay>,

    @Json(name = "friday_schedule")
    val fridaySchedule: List<FridayPrayer>
 ) {
    fun validDays(): List<PrayerDay> {
        val today = Date().asLocalDate()
        return prayerDays.filter { it.date.asLocalDate() >= today }
    }
}