package com.madinaapps.iarmasjid.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.batoulapps.adhan2.Qibla
import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.LocationServices
import com.madinaapps.iarmasjid.utils.adjustedAngleEnd
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

@HiltViewModel
class CompassViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel(), DeviceOrientationListener {

    var qibla: Qibla? = null

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

    private val _compassAngle = MutableStateFlow(0.0)
    val compassAngle = _compassAngle.asStateFlow()

    private val _percentCorrect = MutableStateFlow(-1.0f)
    val percentCorrect = _percentCorrect.asStateFlow()

    private val fusedOrientationProviderClient = LocationServices
        .getFusedOrientationProviderClient(context)

    override fun onDeviceOrientationChanged(orientation: DeviceOrientation) {
        _currentOrientation.value = orientation
        val currentQibla = qibla
        if (currentQibla != null) {
            var newAngle = currentQibla.direction - orientation.headingDegrees
            if (newAngle < 0) {
                newAngle += 360
            }
            _compassAngle.value = _compassAngle.value.adjustedAngleEnd(newAngle)
            _percentCorrect.value = calculatePercentCorrect()
        }
    }

    private fun normalizedSmallestAngle(): Float? {
        var correctedAngle = _currentOrientation.value?.headingDegrees ?: return null
        correctedAngle -= qibla?.direction?.toFloat() ?: return null
        while (correctedAngle < 0) { correctedAngle += 360 }
        correctedAngle -= (360 * (floor(correctedAngle / 360)))

        return if (correctedAngle > 180) { correctedAngle - 360 } else { correctedAngle }
    }

    private fun calculatePercentCorrect(): Float {
        val normalizedSmallestAngle = normalizedSmallestAngle() ?: return -1.0f
        return min(1.0f, 1.2f - (abs(normalizedSmallestAngle)/10f))
    }
}

fun DeviceOrientation.deviation() : Float {
    return if (this.hasConservativeHeadingErrorDegrees()) {
        this.conservativeHeadingErrorDegrees
    } else {
        this.headingErrorDegrees
    }
}
