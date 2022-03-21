package org.raleighmasjid.iar.composable.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.LocalNavController
import org.raleighmasjid.iar.NavigationItem
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.model.json.Event
import org.raleighmasjid.iar.ui.theme.*
import org.raleighmasjid.iar.utils.formatToTime

@Composable
fun eventRow(event: Event) {
    val colors = if (MaterialTheme.colors.isLight) LightColorMode() else DarkColorMode()
    val navController = LocalNavController.current

    fun timeText(): String {
        if (event.allDay) {
            return "All Day"
        } else {
            return "${event.start.formatToTime()} - ${event.end.formatToTime()}"
        }
    }

    Box(modifier = Modifier.clickable {
        navController.navigate(NavigationItem.webRoute(event.url))
    }) {
        Row(modifier = Modifier
            .padding(vertical = 14.dp)
            .padding(start = 32.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1.0f)) {
                Text(event.title,
                    color = colors.prayerColor(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("${event.start.formatToTime()} - ${event.end.formatToTime()}",
                        color = colors.postTextColor())
                    Text(timeText(),
                        color = secondaryTextColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal)
                    Icon(
                        painterResource(id = R.drawable.ic_repeat_icon),
                        contentDescription = null,
                        tint = colors.postTextColor(),
                        modifier = Modifier.size(16.dp, 14.dp))
                }
                Text(event.description,
                    color = tertiaryTextcolor,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp)
            }

            Icon(
                painterResource(id = R.drawable.ic_nav_chevron),
                contentDescription = null,
                tint = colors.buttonTint(),
                modifier = Modifier.size(6.dp, 12.dp))
        }
    }
}