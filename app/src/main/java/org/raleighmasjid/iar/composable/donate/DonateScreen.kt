package org.raleighmasjid.iar.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.R
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.ui.theme.secondaryTextColor

@Composable
fun DonateScreen() {
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
            modifier = Modifier.weight(1f, fill = false)
        )
        Text(
            "Your Masjid relies on the generous people of this community to keep its doors open. Consider donating to help cover our running costs.",
            textAlign = TextAlign.Center,
            color = secondaryTextColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 48.dp)
        )
        OutlinedButton(
            onClick = { /* Do something! */ },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = darkGreen.copy(alpha = 0.1f),
                contentColor = darkGreen
            ),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(27.dp),
            modifier = Modifier.fillMaxWidth()
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