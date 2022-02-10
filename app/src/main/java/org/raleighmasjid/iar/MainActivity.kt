package org.raleighmasjid.iar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.raleighmasjid.iar.composable.PrayerTimesScreen
import org.raleighmasjid.iar.ui.theme.IARTheme
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by lazy { PrayerTimesViewModel(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.darkGreen)
            IARTheme {
                PrayerTimesScreen(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPrayerTimes()
    }
}

