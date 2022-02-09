package org.raleighmasjid.iar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.ui.theme.lightGreen
import org.raleighmasjid.iar.utils.formatToTime
import java.util.*

@Composable
fun PrayerTimeRow(prayer: Prayer, adhan: Date?, iqamah: Date?, current: Boolean) {
    val bgColor: Color = if (current) lightGreen else Color.White
    var isChecked by remember { mutableStateOf<Boolean>(false) }
    Row(
        modifier = Modifier
            .background(bgColor)
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f, true), contentAlignment = Alignment.CenterStart) {
            if (current) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = "Current",
                    modifier = Modifier.size(6.dp, 6.dp).offset((-12).dp)
                )
            }
            Text(
                prayer.title(),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            adhan?.formatToTime() ?: "-:--",
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
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            modifier = Modifier.size(61.dp, 41.dp)) {
            Box(contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(end = 20.dp)
            ) {
                var buttonImage = R.drawable.ic_alarm
                var buttonTint = Color.Black.copy(alpha = 0.5f)
                if (isChecked) {
                    buttonImage = R.drawable.ic_alarm_fill
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
}