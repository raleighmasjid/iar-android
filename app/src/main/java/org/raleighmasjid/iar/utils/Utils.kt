package org.raleighmasjid.iar.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class Utils {
    companion object {
        private const val encodingCharset = "UTF-8"

        fun encodeURL(url: String): String {
            return URLEncoder.encode(url, encodingCharset)
        }

        fun decodeURL(url: String): String {
            return URLDecoder.decode(url, encodingCharset)
        }

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

        fun formatDuration(durationMS: Long): String {
            val duration = durationMS / 1000
            val totalMinutes = duration / 60
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            val seconds = duration % 60

            var outputComponents = mutableListOf<String>()
            if (hours > 0) {
                outputComponents.add("$hours hr")
            }
            if (minutes > 0) {
                outputComponents.add("$minutes min")
            }
            if (hours == 0L) {
                outputComponents.add("$seconds sec")
            }
            return outputComponents.joinToString(separator = ", ")
        }
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