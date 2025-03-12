package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.LocalNavController
import com.madinaapps.iarmasjid.NavigationItem
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.json.Event
import com.madinaapps.iarmasjid.utils.formatToTime

@Composable
fun EventRow(event: Event) {
    val navController = LocalNavController.current

    val timeText: String = if (event.allDay) "All Day" else "${event.start.formatToTime()} - ${event.end.formatToTime()}"

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
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(timeText,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal)
                    Icon(
                        painterResource(id = R.drawable.ic_repeat_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(16.dp, 14.dp))
                }
                Text(event.description,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp)
            }

            Icon(
                painterResource(id = R.drawable.ic_nav_chevron),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(6.dp, 12.dp))
        }
    }
}