package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.madinaapps.iarmasjid.LocalNavController
import com.madinaapps.iarmasjid.NavigationItem
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.json.Post
import com.madinaapps.iarmasjid.ui.theme.secondaryText
import com.madinaapps.iarmasjid.ui.theme.tertiaryText
import com.madinaapps.iarmasjid.utils.formatToDay

@Composable
fun PostRow(post: Post) {
    val navController = LocalNavController.current

    Box(modifier = Modifier.clickable {
        navController.navigate(NavigationItem.webRoute(post.url))
    }) {
        Row(modifier = Modifier
            .padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val imageData: Any? = post.image ?: R.drawable.news_placeholder
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageData
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.background),
                contentScale = ContentScale.Crop
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1.0f)) {
                Text(post.title,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Text(post.text,
                    color = MaterialTheme.colors.secondaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp)
                Text(post.date.formatToDay(),
                    color = MaterialTheme.colors.tertiaryText,
                    fontSize = 12.sp)
            }

            Icon(
                painterResource(id = R.drawable.ic_nav_chevron),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(6.dp, 12.dp))
        }
    }
}