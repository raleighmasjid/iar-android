package org.raleighmasjid.iar.viewModel

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.data.PrayerTimesRepository
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.model.PrayerTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(val dataStoreManager: DataStoreManager) : ViewModel() {
    var prayerDay by mutableStateOf<PrayerDay?>(null)
    var upcoming by mutableStateOf<PrayerTime?>(null)
    var timeRemaining by mutableStateOf<Long>(0)

    private var prayerDays = listOf<PrayerDay>()
        private set(value) {
            field = value
            prayerDay = value.firstOrNull { DateUtils.isToday(it.date.time) }
            updateNextPrayer()
        }

    private var timer: CountDownTimer? = null

    private val repository = PrayerTimesRepository(dataStoreManager)

    init {
        Log.d("INFO", "init view model")
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                updateNextPrayer()
            }

            override fun onFinish() { }
        }.start()

        viewModelScope.launch {
            repository.updates.collect { result ->
                result.onSuccess {
                    prayerDays = it
                }.onFailure {
                    Log.d("INFO", "prayer times failure: $it")
                    // TODO display alert, maybe as toast?
                }
            }
        }

        viewModelScope.launch {
            val flows = Prayer.values().map { prayer -> dataStoreManager.getNotificationEnabled(prayer).map { Pair(prayer, it) } }
            val combinedFlows = combine(flows = flows) { it }
            combinedFlows.collect { newValue ->
                Log.d("INFO", "new alarm values")
                newValue.forEach {
                    Log.d("INFO", "${it.first} is ${it.second}")
                }
            }
        }
    }

    private fun updateNextPrayer() {
        upcoming = PrayerDay.upcomingPrayer(prayerDays)
        val next = upcoming
        if (next != null) {
            timeRemaining = next.adhan.time - Date().time
        }
    }

    fun loadPrayerTimes() {
        viewModelScope.launch {
            repository.fetchPrayerTimes()
        }
    }
}