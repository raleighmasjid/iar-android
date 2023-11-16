package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.utils.formatToDay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerHeader(prayerDays: List<PrayerDay>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()
    val prayerDay: PrayerDay? = prayerDays.elementAtOrNull(pagerState.currentPage)
    val canGoBack: Boolean = pagerState.currentPage > 0
    val canGoForward: Boolean = pagerState.currentPage < prayerDays.count() - 1
    val dateText: String = prayerDay?.date?.formatToDay() ?: "Loading..."
    val hijriText: String = prayerDay?.hijri?.fomatted() ?: ""

    Row {
        IconButton(onClick = {
            if (canGoBack) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            }
        },
        enabled = canGoBack,
        modifier = Modifier.size(75.dp, 75.dp)) {
            var buttonTint = MaterialTheme.colors.primary
            if (!canGoBack) {
                buttonTint = MaterialTheme.colors.onBackground.copy(alpha = 0.3f)
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Previous Day",
                modifier = Modifier.size(32.dp, 32.dp),
                tint = buttonTint
            )
        }

        Box(modifier = Modifier.weight(0.9f, fill = true).padding(vertical = 18.dp),
            contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.clickable {
                scope.launch { pagerState.animateScrollToPage(0) }
            }) {
                Text(dateText, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(hijriText, fontWeight = FontWeight.Normal, fontSize = 13.sp)
            }
        }

        IconButton(onClick = {
            if (canGoForward) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            }
        },
        enabled = canGoForward,
        modifier = Modifier.size(75.dp, 75.dp)) {
            var buttonTint = MaterialTheme.colors.primary
            if (!canGoForward) {
                buttonTint = MaterialTheme.colors.onBackground.copy(alpha = 0.3f)
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next Day",
                modifier = Modifier.size(32.dp, 32.dp),
                tint = buttonTint
            )
        }
    }

}