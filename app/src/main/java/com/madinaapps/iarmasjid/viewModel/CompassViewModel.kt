package com.madinaapps.iarmasjid.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class CompassViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel(), DeviceOrientationListener {

    fun startListening() {
        val request = DeviceOrientationRequest.Builder(
            DeviceOrientationRequest.OUTPUT_PERIOD_DEFAULT
        ).build()

        fusedOrientationProviderClient.requestOrientationUpdates(request, Executors.newSingleThreadExecutor(), this)
    }

    fun stopListening() {
        fusedOrientationProviderClient.removeOrientationUpdates(this)
    }

    private val _currentOrientation = MutableStateFlow<DeviceOrientation?>(null)
    val currentOrientation = _currentOrientation.asStateFlow()

    private val fusedOrientationProviderClient = LocationServices
        .getFusedOrientationProviderClient(context)

    override fun onDeviceOrientationChanged(orientation: DeviceOrientation) {
        _currentOrientation.value = orientation
    }
}