package com.madinaapps.iarmasjid.composable.donate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.ui.theme.*

@Composable
fun DonateScreen() {
    val uriHandler = LocalUriHandler.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.donate_graphic),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(top = 24.dp)
        )
        Text(
            "Your Masjid relies on the generous people of this community to keep its doors open. Consider donating to help cover our running costs.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.secondaryText,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 48.dp)
        )
        Button(
            onClick = {uriHandler.openUri("https://donate.raleighmasjid.org/giving")},
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary
            ),
            shape = RoundedCornerShape(27.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text("Donate Now",
                modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonateScreenPreview() {
    DonateScreen()
}