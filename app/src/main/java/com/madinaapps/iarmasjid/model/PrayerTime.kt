package com.madinaapps.iarmasjid.model

import java.util.Date

data class PrayerTime(
    val prayer: Prayer,
    val adhan: Date,
    val iqamah: Date?
) {
    fun timeRemaining(): Long {
        return adhan.time - Date().time
    }
}