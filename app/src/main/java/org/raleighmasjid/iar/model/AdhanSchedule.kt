package org.raleighmasjid.iar.model

import java.util.*

data class AdhanSchedule(
    val fajr: Date,
    val shuruq: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val isha: Date
)