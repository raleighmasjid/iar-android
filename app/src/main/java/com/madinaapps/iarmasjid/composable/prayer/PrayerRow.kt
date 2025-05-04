package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.utils.formatToTime
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

@Composable
fun PrayerRow(prayer: String,
              adhan: Date?,
              iqamah: Date?,
              current: Boolean,
              displayAlarm: Boolean,
              notificationEnabled: StateFlow<Boolean>,
              toggleAction: (Boolean) -> Unit) {
    val bgColor: Color = if (current) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.background
    val notification = notificationEnabled.collectAsState()
    val textColor: Color = if (current) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .background(bgColor)
            .padding(start = 16.dp)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            prayer,
            modifier = Modifier.weight(1f, true),
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            adhan?.formatToTime() ?: " ",
            modifier = Modifier.weight(1f, true),
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )

        Text(
            iqamah?.formatToTime() ?: " ",
            modifier = Modifier.weight(1f, true),
            color = textColor,
            textAlign = TextAlign.End,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )

        IconToggleButton(
            checked = notification.value,
            onCheckedChange = {
                toggleAction(it)
            },
            enabled = displayAlarm,
            modifier = Modifier
                .size(64.dp, 24.dp)
                .alpha(if (displayAlarm) 1.0f else 0.0f)
        ) {
            var buttonImage = R.drawable.ic_alarm_off
            var buttonTint = MaterialTheme.colorScheme.onTertiary
            if (notification.value) {
                buttonImage = R.drawable.ic_alarm_on
                buttonTint = MaterialTheme.colorScheme.primary
            }
            Icon(
                painter = painterResource(id = buttonImage),
                contentDescription = "Alarm",
                tint = buttonTint,
                modifier = Modifier.size(18.dp, 18.dp)
            )
        }
    }
}