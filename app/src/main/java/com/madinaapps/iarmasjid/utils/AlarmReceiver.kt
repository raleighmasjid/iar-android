package com.madinaapps.iarmasjid.utils

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
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
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.ui.theme.AppColors
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import com.madinaapps.iarmasjid.widget.AppWidget
import com.madinaapps.iarmasjid.widget.updateAppWidget
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.util.UUID


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), contentIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val prayerName = intent.getStringExtra(NotificationController.PRAYER_NAME_KEY) ?: return
        val typeName = intent.getStringExtra(NotificationController.NOTIFICATION_TYPE_KEY) ?: return

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, AppWidget::class.java)
        )
        if (!appWidgetIds.isEmpty()) {
            val dataStoreManager = DataStoreManager(context)
            val viewModel = PrayerTimesViewModel(context, dataStoreManager)
            runBlocking {
                viewModel.loadData(cacheOnly = true)
            }
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, viewModel)
            }
        }

        if (typeName == NotificationController.WIDGET_NOTIFICATION_TYPE) {
            return
        }

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
            .setColor(AppColors.primaryFixed.toArgb())
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