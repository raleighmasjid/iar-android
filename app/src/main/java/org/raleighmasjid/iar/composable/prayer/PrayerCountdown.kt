package org.raleighmasjid.iar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.ui.theme.*
import org.raleighmasjid.iar.utils.Utils
import org.raleighmasjid.iar.viewModel.PrayerCountdownViewModel

@Composable
fun PrayerCountdown(viewModel: PrayerCountdownViewModel) {
    val colors = if (MaterialTheme.colors.isLight) LightColorMode() else DarkColorMode()

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

    Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
        Box(modifier = Modifier.background(colors.badgeBackgroundColor(), RoundedCornerShape(8.dp))) {
            Text(
                nextPrayerText(),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color= colors.nextPrayerColor()
            )
        }

        Text(
            countdownText(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        )
    }
}