package com.madinaapps.iarmasjid.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun PrayerDayView(prayerDay: PrayerDay?, dataStoreManager: DataStoreManager, showTaraweeh: Boolean) {
    val currentPrayer = prayerDay?.currentPrayer()
    val scope = rememberCoroutineScope()
    val taraweehAlpha = if (prayerDay?.hasTaraweeh() == true) 1.0f else 0.8f

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Prayer.values().forEach { prayer ->
            PrayerRow(
                prayer = prayer.title(),
                adhan = prayerDay?.adhanTime(prayer),
                iqamah = prayerDay?.iqamahTime(prayer),
                current = currentPrayer == prayer,
                displayAlarm = true,
                notificationEnabled = dataStoreManager.getNotificationEnabled(prayer),
                toggleAction = {
                    scope.launch {
                        dataStoreManager.setNotification(it, prayer)
                    }
                }
            )
        }

        if (showTaraweeh) {
            Box(modifier = Modifier.alpha(taraweehAlpha)) {
                PrayerRow(
                    prayer = "Taraweeh",
                    adhan = null,
                    iqamah = prayerDay?.iqamah?.taraweeh,
                    current = false,
                    displayAlarm = false,
                    notificationEnabled = flowOf(false),
                    toggleAction = { }
                )
            }
        }
    }
}
