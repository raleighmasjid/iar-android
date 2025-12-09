package com.madinaapps.iarmasjid.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import com.madinaapps.iarmasjid.MainActivity
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.Prayer
import com.madinaapps.iarmasjid.utils.RefreshNotificationsWorker
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
            RefreshNotificationsWorker.scheduleOneTime(context)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        val scope = CoroutineScope(Dispatchers.IO)
        val dataStoreManager = DataStoreManager(context)
        val viewModel = PrayerTimesViewModel(context, dataStoreManager)

        // Rerun the update logic immediately with the new size data
        scope.launch {
            viewModel.loadData()
            updateAppWidget(context, appWidgetManager, appWidgetId, viewModel)
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

    val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
    val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
    val isWideLayout = minWidth >= 300
    Log.d("IARDebug", "minWidth: $minWidth, isWideLayout: $isWideLayout")

    if (upcoming == null || today == null) {
        val views = RemoteViews(context.packageName, R.layout.widget_error)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    } else {
        val timeRemaining = upcoming.adhan.time - System.currentTimeMillis()
        val chronometerBase = SystemClock.elapsedRealtime() + timeRemaining
        val prayerName = upcoming.prayer.title()

        val views = when {
            isWideLayout -> RemoteViews(context.packageName, R.layout.prayer_widget_layout)
            else -> RemoteViews(context.packageName, R.layout.small_prayer_widget_layout)
        }

        views.setTextViewText(R.id.tv_date, today.hijri.fomatted())
        views.setTextViewText(R.id.tv_countdown_label, "$prayerName is in")

        views.setChronometer(R.id.chronometer_countdown, chronometerBase, null, true)
        views.setChronometerCountDown(R.id.chronometer_countdown, true)

        if (isWideLayout) {
            val prayerRows = listOf(
                Triple(Prayer.FAJR, R.id.tv_fajr_time, R.id.tv_fajr_name),
                Triple(Prayer.SHURUQ, R.id.tv_sunrise_time, R.id.tv_sunrise_name),
                Triple(Prayer.DHUHR, R.id.tv_duhr_time, R.id.tv_duhr_name),
                Triple(Prayer.ASR, R.id.tv_asr_time, R.id.tv_asr_name),
                Triple(Prayer.MAGHRIB, R.id.tv_maghrib_time, R.id.tv_maghrib_name),
                Triple(Prayer.ISHA, R.id.tv_isha_time, R.id.tv_isha_name)
            )

            prayerRows.forEach { (prayer, timeViewId, labelViewId) ->
                val adhanTime = today.adhanTime(prayer)
                views.setTextViewText(timeViewId, adhanTime.formatToTime())

                val color = when {
                    prayer == viewModel.current?.prayer -> 0xFF30DB5B.toInt() // current prayer
                    adhanTime.time < System.currentTimeMillis() -> 0xCCFFFFFF.toInt() // past time
                    else -> 0xFFFFFFFF.toInt() // upcoming
                }
                views.setTextColor(timeViewId, color)
                views.setTextColor(labelViewId, color)
            }
        } else {
            val prayerTime = upcoming.adhan.formatToTime()
            val combinedText = "$prayerName  $prayerTime"

            val density = context.resources.displayMetrics.density
            val widgetWidthPx = (minWidth * density).toInt()
            val paddingPx = (32 * density).toInt() // 16dp on each side
            val availableWidth = widgetWidthPx - paddingPx

            val paint = Paint().apply {
                textSize = 16 * context.resources.displayMetrics.scaledDensity
                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            }

            val textWidth = paint.measureText(combinedText)

            if (textWidth >= availableWidth) {
                views.setTextViewText(R.id.tv_prayer_name, combinedText)
                views.setTextViewText(R.id.tv_prayer_time, "")
            } else {
                views.setTextViewText(R.id.tv_prayer_name, prayerName)
                views.setTextViewText(R.id.tv_prayer_time, prayerTime)
            }
        }

        views.setOnClickPendingIntent(
            R.id.widget_root,
            appLaunchIntent(context, appWidgetId)
        )

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
