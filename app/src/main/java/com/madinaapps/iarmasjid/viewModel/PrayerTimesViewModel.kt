package com.madinaapps.iarmasjid.viewModel

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.data.PrayerScheduleRepository
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.model.json.FridayPrayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.model.json.PrayerSchedule
import com.madinaapps.iarmasjid.utils.NotificationController
import com.madinaapps.iarmasjid.utils.asLocalDate
import java.util.*
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

    var didResume: Boolean = false

    private var notificationJob: Job? = null
    private var timer: CountDownTimer? = null
    private val repository = PrayerScheduleRepository(dataStoreManager)

    init {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                updateNextPrayer()
            }

            override fun onFinish() { }
        }.start()

        viewModelScope.launch {
            var flows = Prayer.values().map { prayer -> dataStoreManager.getNotificationEnabled(prayer).map { Unit } }.toMutableList()
            flows.add(dataStoreManager.getNotificationType().map { Unit })
            val combinedFlows = combine(flows = flows) { it }
            combinedFlows.collect {
                Log.d("INFO", "firing updateNotifications")
                updateNotifications()
            }
        }
    }

    private fun updateNotifications() {
        notificationJob?.cancel()
        notificationJob = viewModelScope.launch {
            delay(500)
            val enabledPrayers = dataStoreManager.enabledNotifications()
            val type: NotificationType = runBlocking { dataStoreManager.getNotificationType().first() }
            NotificationController.scheduleNotifications(context, prayerDays, enabledPrayers, type)
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
            val today = Date().asLocalDate()
            val validDays = schedule.prayerDays.filter { it.date.asLocalDate().compareTo(today) >= 0 }
            addAll(validDays)
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
                setPrayerData(it)
            }.onFailure {
                error = true
            }
            loading = false
        }
    }
}