package com.madinaapps.iarmasjid.model.json

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.util.Date

@Keep
@JsonClass(generateAdapter = true)
data class IqamahSchedule(
    val fajr: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val isha: Date,
    val taraweeh: Date?
)