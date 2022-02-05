package org.raleighmasjid.iar.model

import java.util.*

data class PrayerTime(
    val prayer: Prayer,
    val adhan: Date,
    val iqamah: Date?
)