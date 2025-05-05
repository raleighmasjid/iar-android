package com.madinaapps.iarmasjid.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun Countdown(targetTime: Long, content: @Composable (remainingTime: Long) -> Unit) {
    var remainingTime by remember {
        mutableLongStateOf(targetTime - System.currentTimeMillis())
    }
    var isRunning by remember { mutableStateOf(false) }

    content.invoke(remainingTime)

    LifecycleResumeEffect(Unit) {
        isRunning = true
        onPauseOrDispose {
            isRunning = false
        }
    }

    LaunchedEffect(isRunning, targetTime) {
        while (isRunning) {
            remainingTime = targetTime - Date().time
            delay(1000 - Date().time % 1000)
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

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

        val localDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("E, MMM d, y")

        val timeFormatter = timeFormatter()

        val dayFormatter = dayFormatter()

        fun formatDuration(durationMS: Long): String {
            val duration = durationMS / 1000
            val totalMinutes = duration / 60
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            val seconds = duration % 60

            val outputComponents = mutableListOf<String>()
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
    val local1 = this.asLocalDate()
    val local2 = comparison.asLocalDate()
    return local1.isEqual(local2)
}

fun Date.asLocalDate(): LocalDate {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Date.formatToTime(): String {
    return Utils.timeFormatter.format(this)
}

fun Date.formatToDay(): String {
    return Utils.dayFormatter.format(this)
}
