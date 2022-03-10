package org.raleighmasjid.iar.composable.news

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.model.json.SpecialAnnouncement
import org.raleighmasjid.iar.ui.theme.currentPrayerBackground
import org.raleighmasjid.iar.ui.theme.darkGreen

@Composable
fun specialHeader(special: SpecialAnnouncement) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = currentPrayerBackground,
            border = BorderStroke(0.5.dp, darkGreen),
            modifier = Modifier
                .padding(16.dp).
                clickable {
                    Log.d("INFO", "clicked special announcement")
                }
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_special_icon),
                        contentDescription = null,
                        tint = darkGreen,
                        modifier = Modifier.size(16.dp, 16.dp))
                    Text(special.title,
                        color = darkGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(special.text,
                    color = Color.Black,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    lineHeight = 20.sp
                )
            }
        }
    }
}