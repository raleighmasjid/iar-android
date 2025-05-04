package com.madinaapps.iarmasjid.viewModel

import android.content.Context
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.data.PrayerScheduleRepository
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.model.json.FridayPrayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.model.json.PrayerSchedule
import com.madinaapps.iarmasjid.utils.NotificationController
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
            val flows = Prayer.entries.map { prayer -> dataStoreManager.getNotificationEnabled(prayer).map { Unit } }.toMutableList()
            flows.add(dataStoreManager.getNotificationType().map { Unit })
            val combinedFlows = combine(flows = flows) { it }
            combinedFlows.drop(1).collect {
                updateNotifications()
            }
        }
    }

    private fun updateNotifications() {
        notificationJob?.cancel()
        notificationJob = viewModelScope.launch {
            delay(500)
            val enabledPrayers = Prayer.entries.filter { dataStoreManager.getNotificationEnabled(it).first() }
            val type: NotificationType = dataStoreManager.getNotificationType().first()
            NotificationController.scheduleNotifications(context, prayerDays, enabledPrayers, type)
        }
    }

    private fun updateNextPrayer() {
        val newUpcoming = PrayerDay.upcomingPrayer(prayerDays)
        if (newUpcoming != upcoming) {
            upcoming = newUpcoming
        }
    }

    private fun setPrayerData(schedule: PrayerSchedule, cached: Boolean) {
        prayerDays.apply {
            clear()
            addAll(schedule.validDays())
        }
        fridayPrayers.apply {
            clear()
            addAll(schedule.fridaySchedule)
        }
        updateNextPrayer()
        if (!cached) {
            updateNotifications()
        }
    }

    fun loadData() {
        loading = true
        viewModelScope.launch {
            repository.getCachedPrayerSchedule()?.also { cache ->
                setPrayerData(cache, true)
            }

            val scheduleResult = repository.fetchPrayerSchedule(forceRefresh = false)
            scheduleResult.onSuccess {
                setPrayerData(it, false)
            }.onFailure {
                error = true
            }
            loading = false
        }
    }
}