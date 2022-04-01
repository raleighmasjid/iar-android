package com.madinaapps.iarmasjid.model

import java.time.Instant
import java.util.*

data class PrayerTime(
    val prayer: Prayer,
    val adhan: Date,
    val iqamah: Date?
) {
    fun notificationTime(): Instant {
        return adhan.toInstant().minusSeconds(60 * prayer.notificationOffset())
    }

    fun timeRemaining(): Long {
        return adhan.time - Date().time
    }
}