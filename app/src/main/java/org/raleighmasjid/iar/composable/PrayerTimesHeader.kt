package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.model.PrayerTime
import org.raleighmasjid.iar.utils.Utils
import org.raleighmasjid.iar.utils.formatToDay

@Composable
fun PrayerTimesHeader(prayerDay: PrayerDay?, upcoming: PrayerTime?, timeRemaining: Long) {

    fun countdownText(): String {
        if (upcoming != null) {
            val remaining = Utils.formatDuration(timeRemaining)
            return "${upcoming.prayer.title()} is in $remaining"
        } else {
            return " "
        }
    }

    fun dateText(): String {
        if (prayerDay != null) {
            return prayerDay.date.formatToDay()
        } else {
            return "Loading..."
        }
    }

    fun hijriText(): String {
        if (prayerDay != null) {
            return prayerDay.hijri.fomatted()
        } else {
            return " "
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(bottom = 10.dp)) {
        Text(countdownText(),
            modifier = Modifier.padding(horizontal = 20.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(dateText())
            Text(hijriText(), fontStyle = FontStyle.Italic)
        }
    }
}