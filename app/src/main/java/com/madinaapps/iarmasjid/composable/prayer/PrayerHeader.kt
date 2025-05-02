package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.model.json.PrayerDay
import com.madinaapps.iarmasjid.utils.formatToDay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerHeader(prayerDays: List<PrayerDay>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val prayerDay: PrayerDay? = prayerDays.elementAtOrNull(pagerState.currentPage)
    val canGoBack: Boolean = pagerState.currentPage > 0
    val canGoForward: Boolean = pagerState.currentPage < prayerDays.count() - 1
    val dateText: String = prayerDay?.date?.formatToDay() ?: "Loading..."
    val hijriText: String = prayerDay?.hijri?.fomatted() ?: ""

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 14.dp)
    ) {
        IconButton(onClick = {
            if (canGoBack) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            }
        },
        enabled = canGoBack,
        modifier = Modifier.size(32.dp, 32.dp)) {
            var buttonTint = MaterialTheme.colorScheme.primary
            if (!canGoBack) {
                buttonTint = MaterialTheme.colorScheme.onTertiary
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous Day",
                modifier = Modifier.size(20.dp, 20.dp),
                tint = buttonTint
            )
        }

        Box(modifier = Modifier.weight(0.9f, fill = true),
            contentAlignment = Alignment.Center) {
            Column(modifier = Modifier
                .clickable(indication = null, interactionSource = interactionSource, onClick = {
                    scope.launch { pagerState.animateScrollToPage(0) }
                })
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Text(dateText, fontWeight = FontWeight.Medium, fontSize = 17.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text(hijriText, fontWeight = FontWeight.Normal, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }

        IconButton(onClick = {
            if (canGoForward) {
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            }
        },
            enabled = canGoForward,
            modifier = Modifier.size(32.dp, 32.dp)) {
            var buttonTint = MaterialTheme.colorScheme.primary
            if (!canGoForward) {
                buttonTint = MaterialTheme.colorScheme.onTertiary
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next Day",
                modifier = Modifier.size(20.dp, 20.dp),
                tint = buttonTint
            )
        }
    }

}