package org.raleighmasjid.iar.model

import java.util.*

data class IqamahSchedule(
    val fajr: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val isha: Date,
    val taraweeh: Date?
)
