package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.ui.theme.IARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IARTheme {
                PrayerTimesScreen()
            }
        }
    }
}

@Composable
fun PrayerTimesScreen(viewModel: PrayerTimesViewModel = viewModel()) {
    val prayerTimes = viewModel.prayerTimes
    if (prayerTimes != null) {
        Column {
            Prayer.values().forEach { prayer ->
                Text("${prayer.title()} is at ${prayerTimes.adhanTime(prayer)}")
            }
        }

    } else {
        Text("Loading...")
    }
}

class PrayerTimesViewModel : ViewModel() {
    var prayerTimes by mutableStateOf<PrayerDay?>(null)

    init {
        loadPrayerTimes()
    }

    private fun loadPrayerTimes() {
        viewModelScope.launch {
            val response = ApiClient.getPrayerTimes()
            prayerTimes = response.firstOrNull()
        }
    }
}
