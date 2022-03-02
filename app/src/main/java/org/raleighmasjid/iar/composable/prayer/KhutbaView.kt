package org.raleighmasjid.iar.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.model.FridayPrayer
import org.raleighmasjid.iar.ui.theme.darkGreen

@Composable
fun KhutbaView(fridayPrayer: FridayPrayer) {
    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
        Card(elevation = 3.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(190.dp)
                .fillMaxWidth()) {
            Image(painter = painterResource(id = R.drawable.ic_card_back),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))) {
                        Text(fridayPrayer.shift,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = darkGreen
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(fridayPrayer.time, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Box(modifier = Modifier.weight(1.0f), contentAlignment = Alignment.CenterStart) {
                    Text(fridayPrayer.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberImagePainter(
                            data = fridayPrayer.imageUrl,
                            builder = {
                                transformations(CircleCropTransformation())
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(fridayPrayer.speaker, color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis)
                        Text(fridayPrayer.description,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KhutbaViewPreview() {
    KhutbaView(
        FridayPrayer(
            "This Is A title",
            "1st Shift",
            "11:30",
            "Imam Misbah Badaway",
            "Imam at the IAR",
            "https://raleighmasjid.org/wp-content/uploads/2021/08/istockphoto-944796774-170667a.jpg"
        )
    )
}
