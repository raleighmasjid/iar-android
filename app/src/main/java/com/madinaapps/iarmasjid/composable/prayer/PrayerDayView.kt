package com.madinaapps.iarmasjid.composable.prayer

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.viewModel.SettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PrayerRowDivider() {
    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outline,
        thickness = 0.5.dp
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrayerDayView(prayerDay: PrayerDay?, current: PrayerTime?, showTaraweeh: Boolean, viewModel: SettingsViewModel = hiltViewModel()) {
    val taraweehAlpha = if (prayerDay?.hasTaraweeh() == true) 1.0f else 0.8f

    var pendingNotification by remember { mutableStateOf<Prayer?>(null) }
    var showPermissionAlert by remember { mutableStateOf(false) }

    val permissionState: PermissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.POST_NOTIFICATIONS else android.Manifest.permission.INTERNET
    )

    LaunchedEffect(permissionState.status.isGranted) {
        val prayer = pendingNotification
        if (prayer != null && permissionState.status.isGranted) {
            viewModel.setNotification(true, prayer)
            pendingNotification = null
        }
    }

    if (showPermissionAlert) {
        NotificationPermissionDialog(
            dismiss = { showPermissionAlert = false},
            cancel = {
                showPermissionAlert = false
                pendingNotification = null
            }
        )
    }

    fun isCurrent(prayer: Prayer): Boolean {
        if (current == null || prayerDay == null) {
            return false
        }

        return current == prayerDay.prayerTimesMap[prayer]
    }

    Column {
        Prayer.entries.forEach { prayer ->
            PrayerRowDivider()
            PrayerRow(
                prayer = prayer.title(),
                adhan = prayerDay?.adhanTime(prayer),
                iqamah = prayerDay?.iqamahTime(prayer),
                current = isCurrent(prayer),
                displayAlarm = true,
                notificationEnabled = viewModel.getNotificationEnabled(prayer),
                toggleAction = {
                    if (!it) {
                        viewModel.setNotification(false, prayer)
                    } else {
                        if (permissionState.status.isGranted) {
                            viewModel.setNotification(true, prayer)
                        } else if (permissionState.status.shouldShowRationale) {
                            pendingNotification = prayer
                            showPermissionAlert = true
                        } else {
                            pendingNotification = prayer
                            permissionState.launchPermissionRequest()
                        }
                    }
                }
            )
        }

        if (showTaraweeh) {
            PrayerRowDivider()
            Box(modifier = Modifier.alpha(taraweehAlpha)) {
                PrayerRow(
                    prayer = "Taraweeh",
                    adhan = null,
                    iqamah = prayerDay?.iqamah?.taraweeh,
                    current = false,
                    displayAlarm = false,
                    notificationEnabled = MutableStateFlow(false),
                    toggleAction = { }
                )
            }
        }
    }
}
