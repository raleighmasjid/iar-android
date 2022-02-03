package org.raleighmasjid.iar.model

import java.util.*

data class PrayerDay(
    val date: Date,
    val hijri: Hijri,
    val adhan: AdhanSchedule,
    val iqamah: IqamahSchedule
) {
    fun adhanTime(prayer: Prayer): Date {
        return when (prayer) {
            Prayer.FAJR -> adhan.fajr
            Prayer.SHURUQ -> adhan.shuruq
            Prayer.DHUHR -> adhan.dhuhr
            Prayer.ASR -> adhan.asr
            Prayer.MAGHRIB -> adhan.maghrib
            Prayer.ISHA -> adhan.isha
        }
    }

    fun iqamahTime(prayer: Prayer): Date? {
        return when (prayer) {
            Prayer.FAJR -> iqamah.fajr
            Prayer.SHURUQ -> null
            Prayer.DHUHR -> iqamah.dhuhr
            Prayer.ASR -> iqamah.asr
            Prayer.MAGHRIB -> iqamah.maghrib
            Prayer.ISHA -> iqamah.isha
        }
    }
}