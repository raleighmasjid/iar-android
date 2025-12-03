package com.madinaapps.iarmasjid.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.data.PrayerScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            val dataStoreManager = DataStoreManager(context)
            val repository = PrayerScheduleRepository(dataStoreManager)
            val schedule = repository.getCachedPrayerSchedule()

            val prayerName = schedule?.let {
                val today = it.validDays().first()
                today.currentPrayer()?.prayer?.toString() ?: "..."
            } ?: "Loading..."

            // There may be multiple widgets active, so update all of them
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, prayerName)
            }
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, prayerName: String) {
    val views = RemoteViews(context.packageName, R.layout.prayer_widget_layout)
    views.setTextViewText(R.id.widget_text, prayerName)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
