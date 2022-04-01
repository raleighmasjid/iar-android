package com.madinaapps.iarmasjid.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.ui.theme.badgeBackground
import com.madinaapps.iarmasjid.utils.Utils
import com.madinaapps.iarmasjid.viewModel.PrayerCountdownViewModel

@Composable
fun PrayerCountdown(viewModel: PrayerCountdownViewModel) {

    fun nextPrayerText(): String {
        val upcoming = viewModel.upcomingPrayer
        if (upcoming != null) {
            return "${upcoming.prayer.title()} is in"
        } else {
            return " "
        }
    }

    fun countdownText(): String {
        if (viewModel.upcomingPrayer != null) {
            return Utils.formatDuration(viewModel.timeRemaining)
        } else {
            return " "
        }
    }

    fun badgeColor(): Color {
        if (viewModel.upcomingPrayer != null) {
            return badgeBackground
        } else {
            return Color(0)
        }
    }

    Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
        Box(modifier = Modifier.background(badgeColor(), RoundedCornerShape(8.dp))) {
            Text(nextPrayerText(),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        Text(countdownText(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        )
    }
}