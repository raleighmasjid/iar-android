package org.raleighmasjid.iar.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import org.raleighmasjid.iar.model.NotificationType
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerTime
import org.raleighmasjid.iar.model.json.PrayerDay
import java.time.Instant

class NotificationController {
    companion object {
        const val PRAYER_NAME_KEY = "PRAYER_NAME"
        const val PRAYER_TIME_KEY = "PRAYER_TIME"
        const val NOTIFICATION_TYPE_KEY = "NOTIFICATION_TYPE"

        private const val MAX_NOTIFICATIONS: Int = 42

        fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationType.values().forEach { type ->
                    val name = "IAR Prayer Times - ${type.title()}"
                    val descriptionText = "Notifications for the IAR prayer times (${type.title()})"
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val channel = NotificationChannel(type.channelId(), name, importance).apply {
                        description = descriptionText

                        val soundUri = type.soundUri(context)
                        if (soundUri != null) {
                            val audioAttributes = AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                                .build()
                            setSound(soundUri, audioAttributes)
                        } else {
                            setSound(null, null)
                        }
                    }
                    // Register the channel with the system
                    val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)
                }
            }
        }

        private fun notificationTimes(prayerDays: List<PrayerDay>, enabledPrayers: List<Prayer>): List<PrayerTime> {
            val now = Instant.now()
            return prayerDays
                .flatMap { it.prayerTimes }
                .filter { enabledPrayers.contains(it.prayer) && it.notificationTime() > now }
        }

        fun scheduleNotifications(context: Context, prayerDays: List<PrayerDay>, enabledPrayers: List<Prayer>) {
            val prayerTimes = notificationTimes(prayerDays, enabledPrayers).take(MAX_NOTIFICATIONS)

            for (index in 0..MAX_NOTIFICATIONS) {
                val prayerTime = prayerTimes.getOrNull(index)
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    this.putExtra(PRAYER_NAME_KEY, prayerTime?.prayer.toString() ?: "")
                    this.putExtra(PRAYER_TIME_KEY, prayerTime?.adhan?.formatToTime() ?: "")
                    // TODO get notification type from dataStoreManager
                    this.putExtra(NOTIFICATION_TYPE_KEY, NotificationType.SAADALGHAMIDI.toString())
                }
                val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(context, index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                } else {
                    PendingIntent.getBroadcast(context, index, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (prayerTime != null) {
                    alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(prayerTime.notificationTime().toEpochMilli(), pendingIntent), pendingIntent)
                    //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, prayerTime.notificationTime().toEpochMilli(), pendingIntent)
                } else {
                    alarmManager.cancel(pendingIntent)
                }
            }
        }
    }
}