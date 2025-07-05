package com.madinaapps.iarmasjid.composable.qibla

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.utils.pxToDp

@Composable
fun CompassArrow(angle: Double, percentCorrect: Float) {

    var compassSize by remember { mutableIntStateOf(0) }

    val rotationAngle by animateFloatAsState(
        targetValue = angle.toFloat(),
        animationSpec = tween(durationMillis = 75, easing = EaseInOut)
    )

    val shadowColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = percentCorrect)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.compass),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .rotate(rotationAngle)
                .height(compassSize.pxToDp())
                .fillMaxWidth()
                .onSizeChanged {
                    compassSize = it.width
                }
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val blurRadius = 50.dp
                        val offset = 48.dp
                        val paint = Paint()
                        val frameworkPaint = paint.asFrameworkPaint()
                        frameworkPaint.maskFilter = BlurMaskFilter(
                            blurRadius.toPx(),
                            BlurMaskFilter.Blur.NORMAL
                        )
                        frameworkPaint.color = shadowColor.toArgb()

                        val leftPixel = offset.toPx()
                        val topPixel = offset.toPx()
                        val rightPixel = size.width - topPixel
                        val bottomPixel = size.height - leftPixel

                        canvas.drawOval(
                            left = leftPixel,
                            top = topPixel,
                            right = rightPixel,
                            bottom = bottomPixel,
                            paint = paint,
                        )
                    }
                }
        )
        Image(
            painter = painterResource(id = R.drawable.kabah),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size((compassSize * 0.26).toInt().pxToDp())
                .fillMaxWidth()
        )
    }
}
