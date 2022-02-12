package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay

@Composable
fun PrayerTimesList(prayerDay: PrayerDay?, dataStoreManager: DataStoreManager) {
    val currentPrayer = prayerDay?.currentPrayer()
    val scope = rememberCoroutineScope()

    Column {
        prayerColumnHeaders()
        Prayer.values().forEach { prayer ->
            PrayerTimeRow(
                prayer = prayer,
                adhan = prayerDay?.adhanTime(prayer),
                iqamah = prayerDay?.iqamahTime(prayer),
                current = currentPrayer == prayer,
                notificationEnabled = dataStoreManager.getNotificationEnabled(prayer),
                toggleAction = {
                    scope.launch {
                        dataStoreManager.setNotification(it, prayer)
                    }
                }
            )
        }
    }
}
