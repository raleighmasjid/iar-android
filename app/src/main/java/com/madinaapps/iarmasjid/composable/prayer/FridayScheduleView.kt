package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.model.json.FridayPrayer

@Composable
fun FridayScheduleView(fridayPrayers: List<FridayPrayer>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Friday Prayers",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 32.dp)) {
            fridayPrayers.forEach {
                KhutbaView(it)
            }
        }
    }
}