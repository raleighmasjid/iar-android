package org.raleighmasjid.iar.viewModel

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.data.PrayerScheduleRepository
import org.raleighmasjid.iar.model.*
import org.raleighmasjid.iar.utils.NotificationController
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        val dataStoreManager: DataStoreManager
    ) : ViewModel() {

    var prayerDays = mutableStateListOf<PrayerDay>()
        private set

    var fridayPrayers = mutableStateListOf<FridayPrayer>()
        private set

    var upcoming by mutableStateOf<PrayerTime?>(null)
        private set

    var error by mutableStateOf(false)

    var loading by mutableStateOf(false)

    private var notificationJob: Job? = null
    private var timer: CountDownTimer? = null
    private val repository = PrayerScheduleRepository(dataStoreManager)

    init {
        Log.d("INFO", "init view model days ${prayerDays.toList()}")
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                updateNextPrayer()
            }

            override fun onFinish() { }
        }.start()

        viewModelScope.launch {
            val flows = Prayer.values().map { prayer -> dataStoreManager.getNotificationEnabled(prayer).map { Pair(prayer, it) } }
            val combinedFlows = combine(flows = flows) { it }
            combinedFlows.collect {
                updateNotifications()
            }
        }
    }

    private fun updateNotifications() {
        notificationJob?.cancel()
        notificationJob = viewModelScope.launch {
            delay(500)
            val enabledPrayers = dataStoreManager.enabledNotifications()
            NotificationController.scheduleNotifications(context, prayerDays, enabledPrayers)
        }
    }

    private fun updateNextPrayer() {
        val newUpcoming = PrayerDay.upcomingPrayer(prayerDays)
        if (newUpcoming != upcoming) {
            upcoming = newUpcoming
        }
    }

    private fun setPrayerData(schedule: PrayerSchedule) {
        prayerDays.apply {
            clear()
            addAll(schedule.prayerDays)
        }
        fridayPrayers.apply {
            clear()
            addAll(schedule.fridaySchedule)
        }
        updateNextPrayer()
        updateNotifications()
    }

    fun fetchLatest() {
        loading = true
        viewModelScope.launch {
            val cached = repository.getCachedPrayerSchedule()
            if (cached != null) {
                setPrayerData(cached)
            }

            val scheduleResult = repository.fetchPrayerSchedule()
            scheduleResult.onSuccess {
                Log.d("INFO", "fetched prayer times")
                setPrayerData(it)
            }.onFailure {
                error = true
            }
            loading = false
        }
    }
}