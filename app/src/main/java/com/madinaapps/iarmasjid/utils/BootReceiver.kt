package com.madinaapps.iarmasjid.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != "com.htc.intent.action.QUICKBOOT_POWERON" &&
            intent.action != "android.intent.action.QUICKBOOT_POWERON") {
            return
        }

        RefreshNotificationsWorker.scheduleOneTime(context)
        RefreshNotificationsWorker.schedulePeriodic(context)
    }
}