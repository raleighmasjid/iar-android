package com.madinaapps.iarmasjid.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import com.madinaapps.iarmasjid.MainActivity
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.utils.formatToTime
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppWidget: AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val scope = CoroutineScope(Dispatchers.IO)
        val dataStoreManager = DataStoreManager(context)
        val viewModel = PrayerTimesViewModel(context, dataStoreManager)

        scope.launch {
            viewModel.loadData()
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, viewModel)
            }
        }
    }
}

internal fun appLaunchIntent(context: Context, requestCode: Int): PendingIntent {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    return PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, viewModel: PrayerTimesViewModel) {
    val upcoming = viewModel.upcoming
    val today = viewModel.today()

    if (upcoming == null || today == null) {
        val views = RemoteViews(context.packageName, R.layout.widget_error)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    } else {
        val timeRemaining = upcoming.adhan.time - System.currentTimeMillis()
        val chronometerBase = SystemClock.elapsedRealtime() + timeRemaining
        val prayerName = upcoming.prayer.title()
        val views = RemoteViews(context.packageName, R.layout.prayer_widget_layout)
        views.setTextViewText(R.id.tv_date, today.hijri.fomatted())
        views.setTextViewText(R.id.tv_countdown_label, "$prayerName is in")
        views.setTextViewText(R.id.tv_prayer_name, prayerName)
        views.setTextViewText(R.id.tv_prayer_time, upcoming.adhan.formatToTime())
        views.setChronometer(R.id.chronometer_countdown, chronometerBase, null, true)
        views.setChronometerCountDown(R.id.chronometer_countdown, true)

        views.setOnClickPendingIntent(
            R.id.widget_root,
            appLaunchIntent(context, appWidgetId)
        )

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
