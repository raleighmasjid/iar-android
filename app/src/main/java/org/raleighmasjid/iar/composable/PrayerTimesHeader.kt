package org.raleighmasjid.iar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.model.PrayerTime
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.utils.Utils
import org.raleighmasjid.iar.utils.formatToDay

@Composable
fun PrayerTimesHeader(prayerDay: PrayerDay?, upcoming: PrayerTime?, timeRemaining: Long) {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        if (upcoming != null) {
            val remaining = Utils.formatDuration(timeRemaining)
            Text("${upcoming.prayer.title()} is in $remaining",
                modifier = Modifier.padding(horizontal = 20.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp
            )
        }
        if (prayerDay != null) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(prayerDay.date.formatToDay())
                Text(prayerDay.hijri.fomatted(), fontStyle = FontStyle.Italic)
            }
        }
        Row(
            modifier = Modifier
                .background(darkGreen)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                "Prayer",
                modifier = Modifier.weight(1f, true),
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                "Adhan",
                modifier = Modifier.weight(1f, true),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                "Iqamah",
                modifier = Modifier.weight(1f, true),
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}