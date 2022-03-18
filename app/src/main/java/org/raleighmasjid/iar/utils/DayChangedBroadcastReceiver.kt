package org.raleighmasjid.iar.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.text.SimpleDateFormat
import java.util.*

abstract class DayChangedBroadcastReceiver : BroadcastReceiver() {
    private var date = Date()
    private val dateFormat by lazy { SimpleDateFormat("yyMMdd", Locale.getDefault()) }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val currentDate = Date()

        if ((action == Intent.ACTION_DATE_CHANGED)) {
            date = currentDate
            onDayChanged()
        }
    }


    abstract fun onDayChanged()

    companion object {
        fun getIntentFilter() = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
        }
    }
}