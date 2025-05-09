package com.madinaapps.iarmasjid.composable.qibla

import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Qibla
import kotlin.math.abs

@Composable
fun CompassView(location: Location) {
    val context = LocalContext.current
    var deviceOrientation by remember { mutableFloatStateOf(0f) }
    var wasFacingQibla by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryColorAlpha = primaryColor.copy(alpha = 0.1f)

    fun isFacingQibla(deviceOrientation: Float, qiblaDirection: Double): Boolean {
        val difference = abs(deviceOrientation - qiblaDirection.toFloat())
        return difference <= 10 || difference >= 350 // Handle the case when crossing 360/0 degrees
    }


    // Calculate Qibla direction and magnetic declination
    val qibla = remember(location) { Qibla(Coordinates(location.latitude, location.longitude)) }
    val magneticDeclination = remember(location) {
        val geomagneticField = GeomagneticField(
            location.latitude.toFloat(),
            location.longitude.toFloat(),
            location.altitude.toFloat(),
            System.currentTimeMillis()
        )
        geomagneticField.declination.toDouble()
    }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        val sensorListener = object : SensorEventListener {
            private var lastAccelerometer = FloatArray(3)
            private var lastMagnetometer = FloatArray(3)
            private var lastAccelerometerSet = false
            private var lastMagnetometerSet = false

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
                    lastAccelerometerSet = true
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
                    lastMagnetometerSet = true
                }

                if (lastAccelerometerSet && lastMagnetometerSet) {
                    SensorManager.getRotationMatrix(
                        rotationMatrix,
                        null,
                        lastAccelerometer,
                        lastMagnetometer
                    )
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)

                    // Convert radians to degrees and normalize to 0-360
                    val azimuthInDegrees =
                        (Math.toDegrees(orientationAngles[0].toDouble()) + 360.0) % 360.0
                    // Adjust for magnetic declination to get true north
                    deviceOrientation = (azimuthInDegrees + magneticDeclination).toFloat()

                    // Check if facing Qibla state changed
                    val isFacingQibla = isFacingQibla(deviceOrientation, qibla.direction)
                    if (isFacingQibla != wasFacingQibla) {
                        wasFacingQibla = isFacingQibla
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            sensorListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            sensorListener,
            magnetometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width.coerceAtMost(size.height) / 2 - 20

            // Draw compass circle
            drawCircle(
                color = primaryColorAlpha,
                radius = radius,
                center = center
            )

            // Draw compass needle
            rotate(deviceOrientation) {
                drawLine(
                    color = primaryColor,
                    start = Offset(center.x, center.y - radius),
                    end = Offset(center.x, center.y + radius),
                    strokeWidth = 4f
                )
            }

            // Draw Qibla indicator
            val qiblaAngle = (qibla.direction - deviceOrientation).toFloat()
            rotate(qiblaAngle) {
                drawLine(
                    color = Color.Red,
                    start = Offset(center.x, center.y),
                    end = Offset(center.x, center.y - radius),
                    strokeWidth = 4f
                )
            }
        }

        Text(
            "Qibla Direction is ${qibla.direction}°",
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