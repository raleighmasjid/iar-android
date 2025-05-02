package com.madinaapps.iarmasjid.viewModel

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.madinaapps.iarmasjid.model.PrayerTime

class PrayerCountdownViewModel(private val upcoming: PrayerTime?) : ViewModel() {
    var upcomingPrayer by mutableStateOf<PrayerTime?>(upcoming)
    var timeRemaining by mutableLongStateOf(upcoming?.timeRemaining() ?: -1)
    private var timer: CountDownTimer? = null

    init {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                updateTimeRemaining()
            }

            override fun onFinish() {}
        }.start()
    }

    fun updateTimeRemaining() {
        if (upcoming != null) {
            timeRemaining = upcoming.timeRemaining()
        }
    }
}
