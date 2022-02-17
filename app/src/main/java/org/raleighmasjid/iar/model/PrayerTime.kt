package org.raleighmasjid.iar.model

import java.time.Instant
import java.util.*

data class PrayerTime(
    val prayer: Prayer,
    val adhan: Date,
    val iqamah: Date?
) {
    fun notificationTime(): Instant {
        return when (prayer) {
            Prayer.FAJR -> adhan.toInstant()
            Prayer.SHURUQ -> adhan.toInstant().minusSeconds(30 * 60)
            Prayer.DHUHR -> adhan.toInstant()
            Prayer.ASR -> adhan.toInstant()
            Prayer.MAGHRIB -> adhan.toInstant()
            Prayer.ISHA -> adhan.toInstant()
        }
    }
}