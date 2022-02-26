package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import dagger.hilt.android.AndroidEntryPoint
import org.raleighmasjid.iar.composable.PrayerScreen
import org.raleighmasjid.iar.ui.theme.IARTheme
import org.raleighmasjid.iar.utils.NotificationController
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: PrayerTimesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.createNotificationChannel(applicationContext)

        setContent {
            IARTheme {
                Scaffold {
                    PrayerScreen(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchLatest()
    }
}
