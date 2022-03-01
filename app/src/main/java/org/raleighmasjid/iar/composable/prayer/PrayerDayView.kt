package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay

@Composable
fun PrayerDayView(prayerDay: PrayerDay?, dataStoreManager: DataStoreManager) {
    val currentPrayer = prayerDay?.currentPrayer()
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Prayer.values().forEach { prayer ->
            PrayerRow(
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
