package org.raleighmasjid.iar.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.text.SimpleDateFormat
import java.util.*

abstract class DayChangedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if ((action == Intent.ACTION_DATE_CHANGED)) {
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