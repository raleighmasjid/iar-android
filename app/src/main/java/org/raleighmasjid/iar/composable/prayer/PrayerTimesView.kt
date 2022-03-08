package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import org.raleighmasjid.iar.data.DataStoreManager
import org.raleighmasjid.iar.model.json.PrayerDay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrayerTimesView(prayerDays: List<PrayerDay>, pagerState: PagerState, dataStoreManager: DataStoreManager) {
    Column {
        prayerColumnHeaders()
        HorizontalPager(count = prayerDays.count(), state = pagerState) { page ->
            PrayerDayView(prayerDays.getOrNull(page), dataStoreManager)
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