package org.raleighmasjid.iar.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.raleighmasjid.iar.MainActivity
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.model.NotificationType
import org.raleighmasjid.iar.model.Prayer
import java.time.Instant
import java.util.*


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), contentIntent, PendingIntent.FLAG_IMMUTABLE)

        val prayerName = intent.getStringExtra(NotificationController.PRAYER_NAME_KEY) ?: return
        val typeName = intent.getStringExtra(NotificationController.NOTIFICATION_TYPE_KEY) ?: return
        val prayerTime = intent.getStringExtra(NotificationController.PRAYER_TIME_KEY) ?: "-"
        Log.d("INFO", "Received Broadcast for prayer $prayerName, type $typeName")
        val prayer: Prayer
        val type: NotificationType
        try {
            prayer = Prayer.valueOf(prayerName)
            type = NotificationType.valueOf(typeName)
        } catch (e: Exception) {
            Log.d("INFO", "Unable to parse extras $e")
            return
        }

        val title = if (prayer.notificationOffset() > 0) "${prayer.title()} is in ${prayer.notificationOffset()} minutes" else prayer.title()

        Log.d("INFO", "building notification with channel ${type.channelId()} sound ${type.soundUri(context)}")
        val builder = NotificationCompat.Builder(context, type.channelId())
            .setContentTitle("$title ($prayerTime)")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(type.soundUri(context))
            .setShowWhen(true)
            .setWhen(Instant.now().toEpochMilli())
        with(NotificationManagerCompat.from(context)) {
            notify(UUID.randomUUID().hashCode(), builder.build())
        }
    }
}