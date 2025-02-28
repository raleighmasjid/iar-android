package com.madinaapps.iarmasjid.composable.prayer

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrayerDayView(prayerDay: PrayerDay?, dataStoreManager: DataStoreManager, showTaraweeh: Boolean) {
    val currentPrayer = prayerDay?.currentPrayer()
    val scope = rememberCoroutineScope()
    val taraweehAlpha = if (prayerDay?.hasTaraweeh() == true) 1.0f else 0.8f

    var pendingToggle by remember { mutableStateOf(Pair<Prayer?, Boolean>(null, false)) }
    var showPermissionAlert by remember { mutableStateOf(false) }

    val permissionState: PermissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.POST_NOTIFICATIONS else android.Manifest.permission.INTERNET
    )

    LaunchedEffect(permissionState.status.isGranted) {
        val prayer = pendingToggle.first
        if (prayer != null && permissionState.status.isGranted) {
            dataStoreManager.setNotification(pendingToggle.second, prayer)
            pendingToggle = Pair(null, false)
        }
    }

    if (showPermissionAlert) {
        NotificationPermissionDialog(
            dismiss = { showPermissionAlert = false},
            cancel = {
                showPermissionAlert = false
                pendingToggle = Pair(null, false)
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Prayer.entries.forEach { prayer ->
            PrayerRow(
                prayer = prayer.title(),
                adhan = prayerDay?.adhanTime(prayer),
                iqamah = prayerDay?.iqamahTime(prayer),
                current = currentPrayer == prayer,
                displayAlarm = true,
                notificationEnabled = dataStoreManager.getNotificationEnabled(prayer),
                toggleAction = {
                    scope.launch {
                        if (permissionState.status.isGranted) {
                            dataStoreManager.setNotification(it, prayer)
                        } else if (permissionState.status.shouldShowRationale) {
                            pendingToggle = Pair(prayer, it)
                            showPermissionAlert = true
                        } else {
                            pendingToggle = Pair(prayer, it)
                            permissionState.launchPermissionRequest()
                        }
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
