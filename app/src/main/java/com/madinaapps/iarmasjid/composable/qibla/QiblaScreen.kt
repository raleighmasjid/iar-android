package com.madinaapps.iarmasjid.composable.qibla

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.madinaapps.iarmasjid.model.LocationState
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
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
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        when {
            !accessGranted -> {
                LocationDenied()
            }
            locationState is LocationState.Error -> {
                LocationError {
                    viewModel.forceRefresh()
                }
            }
            locationState is LocationState.Valid -> {
                val validState = locationState as LocationState.Valid
                CompassView(validState.location)
            }
            else -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
