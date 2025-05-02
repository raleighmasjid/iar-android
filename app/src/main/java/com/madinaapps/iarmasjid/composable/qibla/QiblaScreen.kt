package com.madinaapps.iarmasjid.composable.qibla

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla
import com.madinaapps.iarmasjid.ui.theme.IARTheme

@Composable
fun QiblaScreen(paddingValues: PaddingValues) {
    val coordinates = Coordinates(35.791836480187186, -78.6350442338134)
    val qibla = Qibla(coordinates)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Text("Qibla Direction is ${qibla.direction}")
//        Row {
//            Text("You're Facing ",
//                color = MaterialTheme.colorScheme.onBackground,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Text("Makkah",
//                color = MaterialTheme.colorScheme.primary,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun QiblaScreenPreview() {
    IARTheme { QiblaScreen(paddingValues = PaddingValues()) }
}