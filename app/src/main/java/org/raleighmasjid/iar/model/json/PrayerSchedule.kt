package org.raleighmasjid.iar.model.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PrayerSchedule(
    @Json(name = "prayer_days")
    val prayerDays: List<PrayerDay>,

    @Json(name = "friday_schedule")
    val fridaySchedule: List<FridayPrayer>
 )