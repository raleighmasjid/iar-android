package org.raleighmasjid.iar.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.model.json.PrayerDay
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.utils.formatToDay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrayerHeader(prayerDays: List<PrayerDay>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()

    fun prayerDay(): PrayerDay? {
        return prayerDays.elementAtOrNull(pagerState.currentPage)
    }

    fun canGoBack(): Boolean {
        return pagerState.currentPage > 0
    }

    fun canGoForward(): Boolean {
        return pagerState.currentPage < prayerDays.count() - 1
    }

    fun dateText(): String {
        val day = prayerDay()
        if (day != null) {
            return day.date.formatToDay()
        } else {
            return "Loading..."
        }
    }

    fun hijriText(): String {
        val day = prayerDay()
        if (day != null) {
            return day.hijri.fomatted()
        } else {
            return " "
        }
    }

    Row {
        IconButton(onClick = {
            if (canGoBack()) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            }
        },
        enabled = canGoBack(),
        modifier = Modifier.size(75.dp, 75.dp)) {
            var buttonTint = darkGreen
            if (!canGoBack()) {
                buttonTint = buttonTint.copy(alpha = 0.3f)
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
                Text(dateText(), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(hijriText(), fontWeight = FontWeight.Normal, fontSize = 13.sp)
            }
        }

        IconButton(onClick = {
            if (canGoForward()) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            }
        },
        enabled = canGoForward(),
        modifier = Modifier.size(75.dp, 75.dp)) {
            var buttonTint = darkGreen
            if (!canGoForward()) {
                buttonTint = buttonTint.copy(alpha = 0.3f)
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