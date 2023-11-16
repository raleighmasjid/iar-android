package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.madinaapps.iarmasjid.model.json.FridayPrayer
import com.madinaapps.iarmasjid.ui.theme.primaryLight

@Composable
fun KhutbaView(fridayPrayer: FridayPrayer) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))) {

        Image(painter = painterResource(id = R.drawable.khutba_back),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.TopStart,
            modifier = Modifier.matchParentSize()
        )
        Image(painter = painterResource(id = R.drawable.khutba_decoration),
            contentDescription = null,
            alignment = Alignment.TopStart,
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))) {
                    Text(fridayPrayer.shift,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = primaryLight
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(fridayPrayer.time, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Text(fridayPrayer.title,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = fridayPrayer.imageUrl).apply(block = fun ImageRequest.Builder.() {
                                transformations(CircleCropTransformation())
                            }).build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(fridayPrayer.speaker, color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold)
                    Text(fridayPrayer.description,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light)
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
