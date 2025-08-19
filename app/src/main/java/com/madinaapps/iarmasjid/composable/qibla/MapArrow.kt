package com.madinaapps.iarmasjid.composable.qibla

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.ui.theme.AppColors
import com.madinaapps.iarmasjid.utils.adjustedAngleEnd

@Composable
fun MapArrow(qiblaDirection: Double) {

    var previousDirection by remember { mutableDoubleStateOf(qiblaDirection) }

    var correctedRotationAngle by remember { mutableDoubleStateOf(0.0) }

    val rotationAngle by animateFloatAsState(
        targetValue = correctedRotationAngle.toFloat(),
        animationSpec = tween(durationMillis = 75, easing = EaseInOut)
    )

    LaunchedEffect(qiblaDirection) {
        correctedRotationAngle = previousDirection.adjustedAngleEnd(qiblaDirection)
        previousDirection = correctedRotationAngle
    }

    Box(contentAlignment = Alignment.Center) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.rotate(rotationAngle)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val midPoint = Offset(x = this.size.width / 2.0f, y = this.size.height / 2.0f)
                drawRect(
                    color = AppColors.compassOutline,
                    topLeft = Offset(midPoint.x - 3.dp.toPx(), midPoint.y - this.size.height),
                    size = Size(6.dp.toPx(), this.size.height)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_map_arrow),
                contentDescription = ""
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_map_kaaba),
            contentDescription = ""
        )
    }
}
