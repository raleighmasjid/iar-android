package com.madinaapps.iarmasjid.model

import java.time.Instant
import java.util.Date

data class NotificationEvent(
    val prayer: Prayer,
    val adhan: Date,
    val iqamah: Date?,
    val widgetOnly: Boolean
) {
    fun notificationTime(): Instant {
        return if (widgetOnly) {
            adhan.toInstant()
        } else {
            adhan.toInstant().minusSeconds(60 * prayer.notificationOffset())
        }
    }

    fun shouldSchedule(enabledPrayers: List<Prayer>, hasWidgets: Boolean): Boolean {
        if (prayer == Prayer.SHURUQ) {
            return (widgetOnly && hasWidgets) || (!widgetOnly && enabledPrayers.contains(prayer))
        }

        return enabledPrayers.contains(prayer) || hasWidgets
    }
}