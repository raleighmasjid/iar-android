package com.madinaapps.iarmasjid.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.madinaapps.iarmasjid.MainActivity
import com.madinaapps.iarmasjid.model.NotificationEvent
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.model.json.PrayerDay
import java.time.Instant
import java.util.UUID

class NotificationController {
    companion object {
        const val PRAYER_NAME_KEY = "PRAYER_NAME"
        const val NOTIFICATION_TYPE_KEY = "NOTIFICATION_TYPE"
        const val WIDGET_NOTIFICATION_TYPE = "WIDGET"

        private const val MAX_NOTIFICATIONS: Int = 42

        fun setupNotificationChannels(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationType.entries.forEach { type ->
                    createChannel(type.title(), type.channelId(), type.soundUri(context), context)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createChannel(title: String, channelId: String, soundUri: Uri?, context: Context) {
            val name = "IAR Prayer Times - $title"
            val descriptionText = "Notifications for the IAR prayer times ($title)"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText

                if (soundUri != null) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
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

        private fun notificationEvents(prayerDays: List<PrayerDay>, enabledPrayers: List<Prayer>, hasWidgets: Boolean): List<NotificationEvent> {
            val now = Instant.now()
            val events: MutableList<NotificationEvent> = mutableListOf()
            events.addAll(
                prayerDays
                    .flatMap { it.prayerTimes }
                    .map { NotificationEvent(it.prayer, it.adhan, it.iqamah, false) }
            )
            if (hasWidgets) {
                events.addAll(
                    prayerDays.map { NotificationEvent(Prayer.SHURUQ, it.adhanTime(Prayer.SHURUQ), null, true) }
                )
            }
            return events.filter { it.notificationTime() > now && it.shouldSchedule(enabledPrayers, hasWidgets) }.sortedBy { it.notificationTime() }
        }

        fun scheduleNotifications(context: Context, type: NotificationType, prayerDays: List<PrayerDay>, enabledPrayers: List<Prayer>, widgetsEnabled: Boolean) {
            val events = notificationEvents(prayerDays, enabledPrayers, widgetsEnabled).take(MAX_NOTIFICATIONS)

            for (index in 0..MAX_NOTIFICATIONS) {
                val event = events.getOrNull(index)
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    if (event != null) {
                        this.putExtra(PRAYER_NAME_KEY, event.prayer.toString())

                        if (event.widgetOnly || !enabledPrayers.contains(event.prayer)) {
                            this.putExtra(
                                NOTIFICATION_TYPE_KEY,
                                WIDGET_NOTIFICATION_TYPE
                            )
                        } else if (event.prayer == Prayer.SHURUQ && type != NotificationType.SILENT) {
                            this.putExtra(
                                NOTIFICATION_TYPE_KEY,
                                NotificationType.SHURUQ.toString()
                            )
                        } else {
                            this.putExtra(
                                NOTIFICATION_TYPE_KEY,
                                type.toString()
                            )
                        }
                    }
                }
                val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, index, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (event != null) {
                    val contentIntent = Intent(context, MainActivity::class.java)
                    val alarmIntent: PendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), contentIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(event.notificationTime().toEpochMilli(), alarmIntent), pendingIntent)
                } else {
                    alarmManager.cancel(pendingIntent)
                }
            }
        }
    }
}