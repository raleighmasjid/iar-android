package com.madinaapps.iarmasjid.viewModel

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.utils.NotificationController
import com.madinaapps.iarmasjid.widget.AppWidget
import com.madinaapps.iarmasjid.widget.updateAppWidget
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
class PrayerScreenViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        private val dataStoreManager: DataStoreManager
    ) : ViewModel() {

    val prayerTimes: PrayerTimesViewModel = PrayerTimesViewModel(context, dataStoreManager, ::prayerTimesDidUpdate)

    var didResume: Boolean = false

    private var notificationJob: Job? = null

    init {
        viewModelScope.launch {
            val flows = Prayer.entries.map { prayer -> dataStoreManager.getNotificationEnabled(prayer).map { } }.toMutableList()
            flows.add(dataStoreManager.getNotificationType().map { })
            val combinedFlows = combine(flows = flows) { it }
            combinedFlows.drop(1).collect {
                updateNotifications()
            }
        }
    }

    private fun prayerTimesDidUpdate() {
        updateNotifications()
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, AppWidget::class.java)
        )

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, prayerTimes)
        }
    }


    private fun updateNotifications() {
        notificationJob?.cancel()
        notificationJob = viewModelScope.launch {
            delay(500)
            val enabledPrayers = Prayer.entries.filter { dataStoreManager.getNotificationEnabled(it).first() }
            val type: NotificationType = dataStoreManager.getNotificationType().first()
            NotificationController.scheduleNotifications(context, prayerTimes.prayerDays, enabledPrayers, type)
        }
    }

    fun loadData() {
        viewModelScope.launch {
            prayerTimes.loadData()
        }
    }
}