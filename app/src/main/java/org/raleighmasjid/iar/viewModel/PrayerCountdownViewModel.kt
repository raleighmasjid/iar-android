package org.raleighmasjid.iar.viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.raleighmasjid.iar.model.PrayerTime
import java.util.*

class PrayerCountdownViewModel(val upcoming: PrayerTime?) : ViewModel() {
    var timeRemaining by mutableStateOf<Long>(0)
    private var timer: CountDownTimer? = null

    init {
        Log.d("INFO", "init view model")
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                updateTimeRemaining()
            }

            override fun onFinish() {}
        }.start()

    }

    fun updateTimeRemaining() {
        if (upcoming != null) {
            timeRemaining = upcoming.adhan.time - Date().time
        }
    }
}
