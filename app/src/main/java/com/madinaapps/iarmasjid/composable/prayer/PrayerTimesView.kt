package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.model.PrayerTime
import com.madinaapps.iarmasjid.model.json.PrayerDay

@Composable
fun PrayerTimesView(prayerDays: List<PrayerDay>, current: PrayerTime?, pagerState: PagerState) {
    val showTaraweeh = prayerDays.any { it.hasTaraweeh() }
    Column {
        PrayerColumnHeaders()
        HorizontalPager(state = pagerState) { page ->
            PrayerDayView(prayerDays.getOrNull(page), current, showTaraweeh)
        }
    }
}


@Composable
fun PrayerColumnHeaders() {
    Row(modifier = Modifier.padding(16.dp)) {
        Text(
            "Prayer",
            modifier = Modifier.weight(1f, true),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )
        Text(
            "Adhan",
            modifier = Modifier.weight(1f, true),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            "Iqamah",
            modifier = Modifier.weight(1f, true),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.width(45.dp))
    }
}