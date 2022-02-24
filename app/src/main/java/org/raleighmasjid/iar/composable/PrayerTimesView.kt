package org.raleighmasjid.iar.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.ui.theme.darkGreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrayerTimesView(prayerDays: List<PrayerDay>, pagerState: PagerState, dataStoreManager: DataStoreManager) {
    Log.d("INFO", "recomposing PrayerTimesView with ${prayerDays.toList().map { it.date }}")
    Column {
        prayerColumnHeaders()
        HorizontalPager(count = prayerDays.count(), state = pagerState) { page ->
            PrayerDayView(prayerDays.getOrNull(page), dataStoreManager)
        }
    }
}


@Composable
fun prayerColumnHeaders() {
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
        Spacer(modifier = Modifier.width(40.dp))
    }
}