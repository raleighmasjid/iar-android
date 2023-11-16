package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.data.DataStoreManager
import com.madinaapps.iarmasjid.model.json.PrayerDay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerTimesView(prayerDays: List<PrayerDay>, pagerState: PagerState, dataStoreManager: DataStoreManager) {
    val showTaraweeh = prayerDays.any { it.hasTaraweeh() }
    Column {
        prayerColumnHeaders()
        HorizontalPager(state = pagerState) { page ->
            PrayerDayView(prayerDays.getOrNull(page), dataStoreManager, showTaraweeh)
        }
    }
}


@Composable
fun prayerColumnHeaders() {
    Row(modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)) {
        Text(
            "Prayer",
            modifier = Modifier.weight(1f, true),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Adhan",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Iqamah",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(56.dp))
    }
}