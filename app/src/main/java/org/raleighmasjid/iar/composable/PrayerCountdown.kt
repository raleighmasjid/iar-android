package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.utils.Utils
import org.raleighmasjid.iar.viewModel.PrayerCountdownViewModel

@Composable
fun PrayerCountdown(viewModel: PrayerCountdownViewModel) {
    fun countdownText(): String {
        if (viewModel.upcoming != null) {
            val remaining = Utils.formatDuration(viewModel.timeRemaining)
            return "${viewModel.upcoming.prayer.title()} is in $remaining"
        } else {
            return " "
        }
    }

    Text(countdownText(),
        modifier = Modifier.padding(horizontal = 20.dp).padding(top = 20.dp),
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    )
}