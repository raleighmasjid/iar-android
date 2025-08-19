package com.madinaapps.iarmasjid.composable.qibla

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
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
import com.madinaapps.iarmasjid.model.LocationState
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel
import kotlinx.coroutines.launch

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

    fun refreshUserLocation() {
        viewModel.removeLocationUpdates()
        viewModel.forceRefresh()
        viewModel.startLocationUpdates()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.removeLocationUpdates()
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
            viewModel.startLocationUpdates()
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

    Box(contentAlignment = Alignment.Center, modifier = Modifier.clipToBounds()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = accessGranted, mapType = mapType.value),
            uiSettings = MapUiSettings(rotationGesturesEnabled = false, myLocationButtonEnabled = false, zoomControlsEnabled = false)
        )
        MapArrow(qiblaDirection.direction)
        MapControls(
            qiblaDirection = qiblaDirection.direction,
            followUser = followUser.value,
            mapType = mapType.value,
            followUserAction = {
                followUser.value = true
                refreshUserLocation()
                goToUser()
            },
            mapTypeAction = {
                mapType.value = when (mapType.value) {
                    MapType.HYBRID -> MapType.NORMAL
                    else -> MapType.HYBRID
                }
            }
        )
    }
}