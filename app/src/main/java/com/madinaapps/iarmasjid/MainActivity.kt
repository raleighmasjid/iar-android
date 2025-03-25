package com.madinaapps.iarmasjid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.madinaapps.iarmasjid.navigation.Navigation
import com.madinaapps.iarmasjid.ui.theme.IARTheme
import com.madinaapps.iarmasjid.utils.DayChangedBroadcastReceiver
import com.madinaapps.iarmasjid.utils.NotificationController
import com.madinaapps.iarmasjid.utils.RefreshNotificationsWorker
import com.madinaapps.iarmasjid.viewModel.NewsViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val prayerTimesViewModel: PrayerTimesViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.setupNotificationChannels(applicationContext)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.Transparent.hashCode()))

        setContent {
            IARTheme {
                Navigation(prayerTimesViewModel, newsViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RefreshNotificationsWorker.schedulePeriodic(applicationContext)
        prayerTimesViewModel.loadData()
        prayerTimesViewModel.didResume = true

        newsViewModel.loadData(forceRefresh = false)

        ContextCompat.registerReceiver(
            this,
            dayChangedBroadcastReceiver,
            DayChangedBroadcastReceiver.getIntentFilter(),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(dayChangedBroadcastReceiver)
    }

    private val dayChangedBroadcastReceiver = object : DayChangedBroadcastReceiver() {

        override fun onDayChanged() {
            prayerTimesViewModel.loadData()
            newsViewModel.loadData(forceRefresh = false)
        }
    }
}

