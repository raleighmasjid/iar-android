package org.raleighmasjid.iar.viewModel

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.model.PrayerTime
import java.util.*

class PrayerTimesViewModel : ViewModel() {
    var prayerDay by mutableStateOf<PrayerDay?>(null)
    var upcoming by mutableStateOf<PrayerTime?>(null)
    var timeRemaining by mutableStateOf<Long>(0)

    var prayerDays = listOf<PrayerDay>()
    var timer: CountDownTimer? = null

    init {
        Log.d("INFO", "init view model")
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                updateNextPrayer()
            }

            override fun onFinish() { }
        }.start()
    }

    private fun updateNextPrayer() {
        upcoming = PrayerDay.upcomingPrayer(prayerDays)
        val next = upcoming
        if (next != null) {
            timeRemaining = next.adhan.time - Date().time
        }
    }

    fun loadPrayerTimes() {
        Log.d("INFO", "loadPrayerTimes")
        viewModelScope.launch {
            val response = ApiClient.getPrayerTimes()
            prayerDays = response
            prayerDay = response.first { DateUtils.isToday(it.date.time) }
            updateNextPrayer()
        }
    }
}