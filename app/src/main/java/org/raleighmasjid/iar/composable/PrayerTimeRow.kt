package org.raleighmasjid.iar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.utils.formatToTime
import java.util.*

@Composable
fun PrayerTimeRow(prayer: Prayer, adhan: Date, iqamah: Date?, current: Boolean) {
    val bgAlpha: Float = if (current) 0.1f else 0f
    Row(
        modifier = Modifier
            .background(Color.Green.copy(alpha = bgAlpha))
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Box(modifier = Modifier.weight(1f, true)) {
            if (current) {
                Text(
                    "\u2022",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset((-12).dp, 2.dp)
                )
            }
            Text(
                prayer.title(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            adhan.formatToTime(),
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            iqamah?.formatToTime() ?: "",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.End,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}