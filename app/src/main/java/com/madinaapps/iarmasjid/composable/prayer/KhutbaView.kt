package com.madinaapps.iarmasjid.composable.prayer

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.json.Campus
import com.madinaapps.iarmasjid.model.json.FridayPrayer
import com.madinaapps.iarmasjid.ui.theme.AppColors
import com.madinaapps.iarmasjid.ui.theme.IARTheme

@Composable
fun KhutbaView(fridayPrayer: FridayPrayer) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 24.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
    ) {

        Box(modifier = Modifier
            .matchParentSize()
            .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.khutba_decoration),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.primaryFixed)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        fridayPrayer.shift,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AppColors.primaryFixed
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    fridayPrayer.time,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(fridayPrayer.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = fridayPrayer.imageUrl)
                                .apply(block = fun ImageRequest.Builder.() {
                                    transformations(CircleCropTransformation())
                                }).build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onTertiary)
                    )
                    Column {
                        Text(
                            fridayPrayer.speaker,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            fridayPrayer.description,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KhutbaViewPreviewDark() {
    IARTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        KhutbaView(
            FridayPrayer(
                title = "This Is A title",
                shift = "1st Shift",
                time = "11:30",
                speaker = "Imam Misbah Badaway",
                campus = Campus.atwater,
                description = "Imam at the IAR",
                imageUrl = "https://raleighmasjid.org/wp-content/uploads/2021/08/istockphoto-944796774-170667a.jpg"
            )
        )
    }
    }
}

@Preview
@Composable
fun KhutbaViewPreviewLight() {
    IARTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
            KhutbaView(
                FridayPrayer(
                    title = "This Is A title",
                    shift = "1st Shift",
                    time = "11:30",
                    speaker = "Imam Misbah Badaway",
                    campus = Campus.atwater,
                    description = "Imam at the IAR",
                    imageUrl = "https://raleighmasjid.org/wp-content/uploads/2021/08/istockphoto-944796774-170667a.jpg"
                )
            )
        }
    }
}
