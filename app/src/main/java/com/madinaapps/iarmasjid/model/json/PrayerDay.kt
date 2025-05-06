package com.madinaapps.iarmasjid.model.json

import androidx.annotation.Keep
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.utils.isSameDay
import com.squareup.moshi.JsonClass
import java.util.Date

@Keep
@JsonClass(generateAdapter = true)
data class PrayerDay(
    val date: Date,
    val hijri: Hijri,
    val adhan: AdhanSchedule,
    val iqamah: IqamahSchedule
) {
    companion object {
        fun upcomingPrayer(prayerDays: List<PrayerDay>, time: Date = Date()): PrayerTime? {
            return prayerDays
                .flatMap { it.prayerTimes }
                .sortedWith(compareBy { it.adhan })
                .firstNotNullOfOrNull { if (it.adhan.after(time)) it else null }
        }
    }

    fun currentPrayer(time: Date = Date()): PrayerTime? {
        val map = prayerTimesMap
        return when {
            time.after(adhan.isha) -> {
                return if (time.isSameDay(adhan.isha)) {
                    map[Prayer.ISHA]
                } else {
                    null
                }
            }
            time.after(adhan.maghrib) -> map[Prayer.MAGHRIB]
            time.after(adhan.asr) -> map[Prayer.ASR]
            time.after(adhan.dhuhr) -> map[Prayer.DHUHR]
            time.after(adhan.shuruq) -> map[Prayer.SHURUQ]
            time.after(adhan.fajr) -> map[Prayer.FAJR]
            else -> null
        }
    }

    val prayerTimes: List<PrayerTime> by lazy {
        Prayer.entries.map { PrayerTime(it, adhanTime(it), iqamahTime(it)) }
    }

    val prayerTimesMap: Map<Prayer, PrayerTime> by lazy {
        prayerTimes.associateBy { it.prayer }
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

    fun hasTaraweeh(): Boolean {
        return iqamah.taraweeh != null
    }
}