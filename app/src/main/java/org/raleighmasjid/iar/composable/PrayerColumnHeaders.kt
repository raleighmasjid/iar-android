package org.raleighmasjid.iar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.ui.theme.darkGreen

@Composable
fun prayerColumnHeaders() {
    Row(
        modifier = Modifier
            .background(darkGreen)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            "Prayer",
            modifier = Modifier.weight(1f, true),
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            "Adhan",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            "Iqamah",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(40.dp))
    }
}