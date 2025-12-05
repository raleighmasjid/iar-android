package com.madinaapps.iarmasjid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.madinaapps.iarmasjid.navigation.Navigation
import com.madinaapps.iarmasjid.ui.theme.IARTheme
import com.madinaapps.iarmasjid.utils.DayChangedBroadcastReceiver
import com.madinaapps.iarmasjid.utils.NotificationController
import com.madinaapps.iarmasjid.utils.RefreshNotificationsWorker
import com.madinaapps.iarmasjid.viewModel.NewsViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerScreenViewModel
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val prayerScreenViewModel: PrayerScreenViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()
    private val qiblaViewModel: QiblaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.Transparent.hashCode()))

        NotificationController.setupNotificationChannels(applicationContext)

        setContent {
            IARTheme {
                Navigation(prayerScreenViewModel, newsViewModel, qiblaViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RefreshNotificationsWorker.schedulePeriodic(applicationContext)
        prayerScreenViewModel.loadData()
        prayerScreenViewModel.didResume = true

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
            prayerScreenViewModel.loadData()
            newsViewModel.loadData(forceRefresh = false)
        }
    }
}

