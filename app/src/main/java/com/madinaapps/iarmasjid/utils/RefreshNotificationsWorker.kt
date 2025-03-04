package com.madinaapps.iarmasjid.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.data.PrayerScheduleRepository
import com.madinaapps.iarmasjid.model.NotificationType
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class RefreshNotificationsWorker(private val appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    companion object {
        private fun periodicRequest(): PeriodicWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return PeriodicWorkRequestBuilder<RefreshNotificationsWorker>(24, TimeUnit.HOURS, 6, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(24, TimeUnit.HOURS)
                .addTag("DAILY_REFRESH_NOTIFICATIONS_WORKER")
                .build()
        }

        private fun oneTimeRequest(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return OneTimeWorkRequestBuilder<RefreshNotificationsWorker>()
                .setConstraints(constraints)
                .addTag("ONETIME_REFRESH_NOTIFICATIONS_WORKER")
                .build()
        }

        fun schedulePeriodic(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork("DAILY_REFRESH_NOTIFICATIONS",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    periodicRequest()
                )
        }

        fun scheduleOneTime(context: Context) {
            WorkManager.getInstance(context)
                .enqueue(oneTimeRequest())
        }
    }

    override suspend fun doWork(): Result {
        val dataStoreManager = DataStoreManager(appContext = appContext)
        val enabledPrayers = dataStoreManager.enabledNotifications()
        val repository = PrayerScheduleRepository(dataStoreManager)
        val type: NotificationType = dataStoreManager.getNotificationType().first()
        val scheduleResult = repository.fetchPrayerSchedule(forceRefresh = false)
        if (scheduleResult.isSuccess) {
            val prayerDays = scheduleResult.getOrNull()?.validDays() ?: emptyList()
            if (prayerDays.isNotEmpty() && enabledPrayers.isNotEmpty()) {
                NotificationController.scheduleNotifications(appContext, prayerDays, enabledPrayers, type)
            }
            return Result.success()
        } else {
            return Result.failure()
        }
    }
}