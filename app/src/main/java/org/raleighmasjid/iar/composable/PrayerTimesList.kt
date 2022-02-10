package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.viewModel.AlarmPreferences

@Composable
fun PrayerTimesList(prayerDay: PrayerDay?, alarmPrefs: AlarmPreferences) {
    val currentPrayer = prayerDay?.currentPrayer()

    Column {
        prayerColumnHeaders()
        Prayer.values().forEach { prayer ->
            PrayerTimeRow(
                prayer = prayer,
                adhan = prayerDay?.adhanTime(prayer),
                iqamah = prayerDay?.iqamahTime(prayer),
                current = currentPrayer == prayer,
                alarmPrefs = alarmPrefs
            )
        }
    }
}
