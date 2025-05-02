package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.utils.Utils

@Composable
fun SmallPrayerCountdown(upcoming: PrayerTime?, timeRemaining: Long) {
    val nextPrayerText: String =
        if (upcoming != null) {
            "${upcoming.prayer.title()} is in"
        } else {
            " "
        }

    val countdownText: String =
        if (upcoming != null && timeRemaining >= 0) {
            Utils.formatDuration(timeRemaining)
        } else {
            " "
        }

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$nextPrayerText $countdownText",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.White,
        )
    }
}