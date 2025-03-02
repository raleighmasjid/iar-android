package com.madinaapps.iarmasjid.utils

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.madinaapps.iarmasjid.MainActivity
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.ui.theme.darkGreen
import java.time.Instant
import java.util.UUID


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), contentIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val prayerName = intent.getStringExtra(NotificationController.PRAYER_NAME_KEY) ?: return
        val typeName = intent.getStringExtra(NotificationController.NOTIFICATION_TYPE_KEY) ?: return
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

        val builder = NotificationCompat.Builder(context, type.channelId())
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_stat_onesignal_default)
            .setColor(darkGreen.toArgb())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(type.soundUri(context))
            .setShowWhen(true)
            .setWhen(Instant.now().toEpochMilli())
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(UUID.randomUUID().hashCode(), builder.build())
            }
        }
    }
}