package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.model.json.FridayPrayer

@Composable
fun FridayScheduleView(fridayPrayers: List<FridayPrayer>) {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text("Friday Prayers",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 20.dp)) {
            fridayPrayers.forEach {
                KhutbaView(it)
            }
        }
    }
}