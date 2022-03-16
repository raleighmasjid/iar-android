package org.raleighmasjid.iar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.ui.theme.currentPrayerBackground
import org.raleighmasjid.iar.ui.theme.currentPrayerBorderColor
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.ui.theme.prayerBorderColor
import org.raleighmasjid.iar.utils.formatToTime
import java.util.*

@Composable
fun PrayerRow(prayer: String,
              adhan: Date?,
              iqamah: Date?,
              current: Boolean,
              displayAlarm: Boolean,
              notificationEnabled: Flow<Boolean>,
              toggleAction: (Boolean) -> Unit) {
    val bgColor: Color = if (current) currentPrayerBackground else Color.White
    val notification: Boolean by notificationEnabled.collectAsState(initial = false)
    val borderColor = if (current) currentPrayerBorderColor else prayerBorderColor
    val rowAlpha = if (current || (adhan?.after(Date()) == true)) 1.0f else 0.9f
    val alarmAlpha = if (displayAlarm) 1.0f else 0f

    Row(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .border(width = 0.5.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
            .padding(start = 12.dp)
            .alpha(rowAlpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            prayer,
            modifier = Modifier.weight(1f, true)
                .padding(vertical = 15.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            adhan?.formatToTime() ?: " ",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            iqamah?.formatToTime() ?: " ",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        IconToggleButton(
            checked = notification,
            onCheckedChange = { toggleAction(it) },
            modifier = Modifier.size(61.dp, 41.dp).alpha(alarmAlpha)
        ) {
            var buttonImage = R.drawable.ic_alarm_off
            var buttonTint = Color.Black.copy(alpha = 0.5f)
            if (notification) {
                buttonImage = R.drawable.ic_alarm_on
                buttonTint = darkGreen
            }
            Icon(
                painter = painterResource(id = buttonImage),
                contentDescription = "Alarm",
                tint = buttonTint,
                modifier = Modifier.size(16.dp, 16.dp)
            )
        }
    }
}