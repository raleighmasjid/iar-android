package org.raleighmasjid.iar.composable

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
import org.raleighmasjid.iar.model.PrayerDay
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

    Row(modifier = Modifier.padding(20.dp)) {
        IconButton(onClick = {
            if (canGoBack()) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            }
        },
        enabled = canGoBack(),
        modifier = Modifier.weight(0.1f, fill = false)) {
            var buttonTint = darkGreen
            if (!canGoBack()) {
                buttonTint = buttonTint.copy(alpha = 0.3f)
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_left_chevron),
                contentDescription = "Previous Day",
                modifier = Modifier.size(45.dp, 45.dp),
                tint = buttonTint
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(0.9f, fill = true),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(dateText(), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(hijriText(), fontWeight = FontWeight.Normal, fontSize = 13.sp)
        }

        IconButton(onClick = {
            if (canGoForward()) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            }
        },
        enabled = canGoForward(),
        modifier = Modifier.weight(0.1f, fill = false)) {
            var buttonTint = darkGreen
            if (!canGoForward()) {
                buttonTint = buttonTint.copy(alpha = 0.3f)
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_right_chevron),
                contentDescription = "Next Day",
                modifier = Modifier.size(45.dp, 45.dp),
                tint = buttonTint
            )
        }
    }

}