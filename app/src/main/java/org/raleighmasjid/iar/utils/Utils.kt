package org.raleighmasjid.iar.utils

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class Utils {
    companion object {
        private fun timeFormatter(): SimpleDateFormat {
            val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
            timeFormatter.timeZone = TimeZone.getTimeZone("America/New_York")
            return timeFormatter
        }

        private fun dayFormatter(): SimpleDateFormat {
            val timeFormatter = SimpleDateFormat("E, MMM d, y", Locale.getDefault())
            timeFormatter.timeZone = TimeZone.getTimeZone("America/New_York")
            return timeFormatter
        }

        val timeFormatter = timeFormatter()

        val dayFormatter = dayFormatter()
    }
}

fun Date.isSameDay(comparison: Date): Boolean {
    val local1 = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val local2 = comparison.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return local1.isEqual(local2)
}

fun Date.formatToTime(): String {
    return Utils.timeFormatter.format(this)
}

fun Date.formatToDay(): String {
    return Utils.dayFormatter.format(this)
}