package com.madinaapps.iarmasjid.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

abstract class DayChangedBroadcastReceiver : BroadcastReceiver() {

    companion object {
        fun getIntentFilter() = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_DATE_CHANGED) {
            onDayChanged()
        }
    }

    abstract fun onDayChanged()
}