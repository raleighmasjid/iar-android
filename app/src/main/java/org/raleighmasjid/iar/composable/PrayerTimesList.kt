package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay

@Composable
fun PrayerTimesList(prayerDay: PrayerDay) {
    val currentPrayer = prayerDay.currentPrayer()
    Column {
        Prayer.values().forEach { prayer ->
            PrayerTimeRow(
                prayer = prayer,
                adhan = prayerDay.adhanTime(prayer),
                iqamah = prayerDay.iqamahTime(prayer),
                current = currentPrayer == prayer
            )
        }
    }
}