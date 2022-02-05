package org.raleighmasjid.iar.model

import org.raleighmasjid.iar.utils.isSameDay
import java.util.*

data class PrayerDay(
    val date: Date,
    val hijri: Hijri,
    val adhan: AdhanSchedule,
    val iqamah: IqamahSchedule
) {
    companion object {
        fun upcomingPrayer(prayerDays: List<PrayerDay>, time: Date = Date()): PrayerTime? {
            return prayerDays
                .flatMap { it.prayerTimes() }
                .sortedWith(compareBy { it.adhan })
                .firstNotNullOfOrNull { if (it.adhan.after(time)) it else null }
        }
    }

    fun prayerTimes(): List<PrayerTime> {
        return Prayer.values().map { PrayerTime(it, adhanTime(it), iqamahTime(it)) }
    }

    fun currentPrayer(time: Date = Date()): Prayer? {
        return when {
            time.after(adhan.isha) -> {
                return if (time.isSameDay(adhan.isha)) {
                    Prayer.ISHA
                } else {
                    null
                }
            }
            time.after(adhan.maghrib) -> Prayer.MAGHRIB
            time.after(adhan.asr) -> Prayer.ASR
            time.after(adhan.dhuhr) -> Prayer.DHUHR
            time.after(adhan.shuruq) -> Prayer.SHURUQ
            time.after(adhan.fajr) -> Prayer.FAJR
            else -> null
        }
    }

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