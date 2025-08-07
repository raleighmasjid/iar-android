package com.madinaapps.iarmasjid.composable.qibla

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.madinaapps.iarmasjid.model.LocationState
import com.madinaapps.iarmasjid.model.QiblaMode
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel

@Composable
fun tabTextColor(mode: QiblaMode, selectedMode: QiblaMode): Color {
    return if (mode == selectedMode) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.onSecondary
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QiblaScreen(
    paddingValues: PaddingValues,
    viewModel: QiblaViewModel
) {
    val locationState by viewModel.locationState.collectAsState()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val accessGranted = locationPermissions.permissions.any { it.status.isGranted }

    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }


    // Request permissions when screen becomes visible
    LaunchedEffect(Unit) {
        if (!accessGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(accessGranted) {
        if (accessGranted) {
            viewModel.getCurrentLocation()
        }
    }

    LaunchedEffect(locationState) {
        when (val state = locationState) { // Assign to a local variable
            is LocationState.Valid -> {
                val location = state.location // No need for explicit cast
                Log.d("IARDebug", location.toString())
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 15f)
            }
            LocationState.Error -> {
                // Handle error state
            }
            LocationState.Pending -> {
                // Handle pending state
            }
        }
    }


    LaunchedEffect(cameraPositionState.position) {
        Log.d("IARDebug", cameraPositionState.position.toString())
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        SecondaryTabRow(
            viewModel.mode.index,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {}
        ) {
            QiblaMode.entries.forEach {
                Tab(
                    selected = viewModel.mode == it,
                    onClick = { viewModel.mode = it }
                ) {
                    Text(it.title,
                        fontWeight = FontWeight.Medium,
                        color = tabTextColor(it, viewModel.mode),
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                }
            }
        }
        when(viewModel.mode) {
            QiblaMode.MAP -> GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
            )
            QiblaMode.COMPASS -> QiblaCompass(viewModel)
        }
    }
}
