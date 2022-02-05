package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@Composable
fun PrayerTimesScreen(viewModel: PrayerTimesViewModel = viewModel()) {
    val prayerDay = viewModel.prayerDay

    Column {
        Text(
            "Prayer Times",
            modifier = Modifier.padding(horizontal = 18.dp).padding(top = 20.dp, bottom = 6.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )
        PrayerTimesHeader(prayerDay, viewModel.upcoming, viewModel.timeRemaining)
        if (prayerDay != null) {
            PrayerTimesList(prayerDay)
        } else {
            Text("Loading...", modifier = Modifier.padding(20.dp))
        }
    }
}