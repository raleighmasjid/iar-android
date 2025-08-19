package com.madinaapps.iarmasjid.composable.qibla

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.MapType
import com.madinaapps.iarmasjid.R
import java.util.Locale

@Composable
fun MapControls(qiblaDirection: Double, followUser: Boolean, mapType: MapType, followUserAction: () -> Unit, mapTypeAction: () -> Unit) {
    Row {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(Modifier.weight(1f))
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))

            ) {
                Text(
                    text = "Qibla ${String.format(Locale.getDefault(), "%.1f", qiblaDirection)}°",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            Spacer(Modifier.height(32.dp))
        }
        Spacer(Modifier.weight(1f))
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    followUserAction()
                },
                contentPadding = PaddingValues(all = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (followUser) { R.drawable.ic_user_location_fill } else { R.drawable.ic_user_location }),
                    contentDescription = "Map Type",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(16.dp, 16.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    mapTypeAction()
                },
                contentPadding = PaddingValues(all = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (mapType == MapType.HYBRID) { R.drawable.ic_globe } else { R.drawable.ic_map }),
                    contentDescription = "Map Type",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(16.dp, 16.dp)
                )
            }
        }
    }
}