package com.madinaapps.iarmasjid.composable.qibla

import android.Manifest
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.LocationState
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QiblaMap(viewModel: QiblaViewModel) {
    val locationState by viewModel.locationState.collectAsState()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val accessGranted = locationPermissions.permissions.any { it.status.isGranted }

    val iarCoordinates = LatLng(35.78977019151847, -78.691211012544)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(iarCoordinates, 18f)
    }

    val mapType = remember { mutableStateOf(MapType.HYBRID) }
    val followUser = remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    val qiblaDirection = remember(cameraPositionState.position.target) {
        val location = cameraPositionState.position.target
        Qibla(Coordinates(location.latitude, location.longitude))
    }

    fun goToUser() {
        val state = locationState
        if (state is LocationState.Valid) {
            val location = state.location
            coroutineScope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude),
                        19f
                    )
                )
            }
        }
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
        if (followUser.value) {
            goToUser()
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
            followUser.value = false
        }
    }

    Box {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = accessGranted, mapType = mapType.value),
            uiSettings = MapUiSettings(rotationGesturesEnabled = false)
        )
        Row {
            Column(modifier = Modifier.padding(8.dp)) {
                Spacer(Modifier.weight(1f))
                Box(modifier = Modifier
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))

                ) {
                    Text(
                        text = "Qibla ${String.format(Locale.getDefault(), "%.1f", qiblaDirection.direction)}°",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
            Spacer(Modifier.weight(1f))
            Column(modifier = Modifier.padding(8.dp)) {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        followUser.value = true
                        goToUser()
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
                        painter = painterResource(id = if (followUser.value) { R.drawable.ic_user_location_fill } else { R.drawable.ic_user_location }),
                        contentDescription = "Map Type",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp, 16.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        mapType.value = when (mapType.value) {
                            MapType.HYBRID -> MapType.NORMAL
                            else -> MapType.HYBRID
                        }
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
                        painter = painterResource(id = if (mapType.value == MapType.HYBRID) { R.drawable.ic_globe } else { R.drawable.ic_map }),
                        contentDescription = "Map Type",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp, 16.dp)
                    )
                }
                Spacer(Modifier.height(96.dp))
            }
        }
    }
}