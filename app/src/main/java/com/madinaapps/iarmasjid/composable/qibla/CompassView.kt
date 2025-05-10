package com.madinaapps.iarmasjid.composable.qibla

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla
import com.madinaapps.iarmasjid.viewModel.CompassViewModel
import java.util.Locale
import kotlin.math.abs

@Composable
fun CompassView(
    location: Location,
    viewModel: CompassViewModel = hiltViewModel<CompassViewModel>()
) {
    val context = LocalContext.current
    val currentOrientation by viewModel.currentOrientation.collectAsState()
    var wasFacingQibla by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    fun isFacingQibla(deviceOrientation: Float, qiblaDirection: Double): Boolean {
        val difference = abs(deviceOrientation - qiblaDirection.toFloat())
        return difference <= 10 || difference >= 350 // Handle the case when crossing 360/0 degrees
    }

    val qibla = remember(location) { Qibla(Coordinates(location.latitude, location.longitude)) }

    DisposableEffect(context) {
        viewModel.startListening()
        onDispose {
            viewModel.stopListening()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val orientation = currentOrientation) {
            null -> Text(
                "Waiting for heading data",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            else -> {
                Text(
                    "Current orientation ${String.format(Locale.getDefault(), "%.1f", orientation.headingDegrees)}°",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    "Max error ${String.format(Locale.getDefault(), "%.1f", orientation.headingErrorDegrees)}°",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )

                if (orientation.hasConservativeHeadingErrorDegrees()) {
                    Text(
                        "Conservative error ${String.format(Locale.getDefault(), "%.1f", orientation.conservativeHeadingErrorDegrees)}°",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        Text(
            "Qibla Direction is ${String.format(Locale.getDefault(), "%.1f", qibla.direction)}°",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

        if (wasFacingQibla) {
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    "You're Facing ",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Makkah",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}