package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import org.raleighmasjid.iar.model.FridayPrayer

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FridayScheduleView(fridayPrayers: List<FridayPrayer>) {
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text("Friday Prayers",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp))
        HorizontalPager(count = fridayPrayers.count(), state = pagerState) { page ->
            val fridayPrayer = fridayPrayers.getOrNull(page)
            if (fridayPrayer != null) {
                KhutbaView(fridayPrayer)
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
    }
}