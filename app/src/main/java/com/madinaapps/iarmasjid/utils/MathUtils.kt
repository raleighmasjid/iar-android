package com.madinaapps.iarmasjid.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

fun Double.adjustedAngleEnd(to: Double): Double {
    var end = to
    while (end < this) { end += 360.0 }

    // Mod the distance with 360, shifting by 180 to keep on the same side of a circle
    return (end - this + 180.0).rem(360.0) - 180.0 + this
}
