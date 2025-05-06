package com.madinaapps.iarmasjid.composable.donate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.ui.theme.IARTheme

@Composable
fun DonateScreen(paddingValues: PaddingValues) {
    val uriHandler = LocalUriHandler.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.donate_graphic),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            "Your Masjid relies on the generous people of this community to keep its doors open. Consider donating to help cover our running costs.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 48.dp)
        )
        Button(
            onClick = {uriHandler.openUri("https://donate.raleighmasjid.org/giving")},
            elevation = null,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Donate Now",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonateScreenPreview() {
    IARTheme { DonateScreen(paddingValues = PaddingValues()) }
}