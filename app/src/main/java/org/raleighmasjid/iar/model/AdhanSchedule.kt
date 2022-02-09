package org.raleighmasjid.iar.model

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AdhanSchedule(
    val fajr: Date,
    val shuruq: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val isha: Date
)