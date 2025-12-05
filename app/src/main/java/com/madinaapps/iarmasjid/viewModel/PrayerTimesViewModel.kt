package com.madinaapps.iarmasjid.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.data.PrayerScheduleRepository
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.model.json.FridayPrayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.model.json.PrayerSchedule
import com.madinaapps.iarmasjid.utils.isToday
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrayerTimesViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        dataStoreManager: DataStoreManager,
        val onPrayerTimesUpdated: (() -> Unit)? = null
    ) : ViewModel() {

    var prayerDays = mutableStateListOf<PrayerDay>()
        private set

    var fridayPrayers = mutableStateListOf<FridayPrayer>()
        private set

    var upcoming by mutableStateOf<PrayerTime?>(null)
        private set

    var current by mutableStateOf<PrayerTime?>(null)
        private set

    var error by mutableStateOf(false)

    var loading by mutableStateOf(false)

    private val repository = PrayerScheduleRepository(dataStoreManager)

    fun today(): PrayerDay? {
        return prayerDays.firstOrNull { it.date.isToday() }
    }

    fun updateNextPrayer() {
        val newUpcoming = PrayerDay.upcomingPrayer(prayerDays)
        if (newUpcoming != upcoming) {
            upcoming = newUpcoming
        }

        var updatedCurrent: PrayerTime? = null
        for (prayerDay in prayerDays) {
            val newCurrent = prayerDay.currentPrayer()
            if (newCurrent != null) {
                updatedCurrent = newCurrent
            }
        }
        if (updatedCurrent != current) {
            current = updatedCurrent
        }
    }

    fun setPrayerData(schedule: PrayerSchedule, cached: Boolean) {
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
            onPrayerTimesUpdated?.invoke()
        }
    }

    suspend fun loadData() {
        loading = true
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