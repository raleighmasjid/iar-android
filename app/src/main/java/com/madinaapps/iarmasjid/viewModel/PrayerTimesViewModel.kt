package com.madinaapps.iarmasjid.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madinaapps.iarmasjid.data.PrayerScheduleRepository
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.model.json.FridayPrayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.model.json.PrayerSchedule
import com.madinaapps.iarmasjid.utils.isToday
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrayerTimesUiState(
    val prayerDays: List<PrayerDay> = emptyList(),
    val fridayPrayers: List<FridayPrayer> = emptyList(),
    val upcoming: PrayerTime? = null,
    val current: PrayerTime? = null,
    val error: Boolean = false,
    val loading: Boolean = false
) {
    fun today(): PrayerDay? {
        return prayerDays.firstOrNull { it.date.isToday() }
    }
}

class PrayerTimesViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        private val repository: PrayerScheduleRepository,
        val onPrayerTimesUpdated: (() -> Unit)? = null
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerTimesUiState())
    val uiState: StateFlow<PrayerTimesUiState> = _uiState.asStateFlow()

    // Compatibility properties for internal logic (should eventually be removed)
    val prayerDays: List<PrayerDay> get() = _uiState.value.prayerDays

    fun updateNextPrayer() {
        val newUpcoming = PrayerDay.upcomingPrayer(_uiState.value.prayerDays)
        
        var updatedCurrent: PrayerTime? = null
        for (prayerDay in _uiState.value.prayerDays) {
            val newCurrent = prayerDay.currentPrayer()
            if (newCurrent != null) {
                updatedCurrent = newCurrent
            }
        }

        _uiState.update { it.copy(upcoming = newUpcoming, current = updatedCurrent) }
    }

    private fun setPrayerData(schedule: PrayerSchedule, cached: Boolean) {
        _uiState.update { 
            it.copy(
                prayerDays = schedule.validDays(),
                fridayPrayers = schedule.fridaySchedule
            )
        }
        updateNextPrayer()
        if (!cached) {
            onPrayerTimesUpdated?.invoke()
        }
    }

    suspend fun loadData(cacheOnly: Boolean = false) {
        _uiState.update { it.copy(loading = true) }
        
        repository.getCachedPrayerSchedule()?.also { cache ->
            setPrayerData(cache, true)
        }
        
        if (cacheOnly) {
            _uiState.update { it.copy(loading = false) }
            return
        }

        val scheduleResult = repository.fetchPrayerSchedule(forceRefresh = false)
        scheduleResult.onSuccess {
            setPrayerData(it, false)
        }.onFailure {
            _uiState.update { it.copy(error = true) }
        }
        _uiState.update { it.copy(loading = false) }
    }
}
