package com.madinaapps.iarmasjid.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.PathEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

private val pathForAnimation = Path().apply {
    moveTo(0f, 0f)
    cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
    cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
}

private const val DurationShort1 = 150
private const val DurationMedium1 = 250
private const val DurationMedium2 = 300
private const val DurationLong1 = 450
private const val DurationLong2 = 500
private val EmphasizedEasing = PathEasing(pathForAnimation)
private val EmphasizedAccelerateEasing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

object NavTransition {
    val defaultEnterTransition = fadeIn(animationSpec = tween(DurationShort1))
    val defaultExitTransition = fadeOut(animationSpec = tween(DurationShort1))

    val pushEnterTransition: () -> EnterTransition = {
        fadeIn(
            animationSpec = tween(durationMillis = DurationLong1, easing = EmphasizedEasing)
        ) +
                slideInHorizontally(
                    animationSpec = tween(durationMillis = DurationLong2, easing = EmphasizedEasing)
                ) {
                    it / 2
                }
    }

    val popEnterTransition: () -> EnterTransition = {
        fadeIn(
            animationSpec = tween(durationMillis = DurationLong1, easing = EmphasizedEasing)
        ) +
                slideInHorizontally(
                    animationSpec = tween(durationMillis = DurationLong2, easing = EmphasizedEasing)
                ) {
                    -it / 2
                }
    }

    val pushExitTransition: (Density) -> ExitTransition = { density ->
        fadeOut(
            animationSpec = tween(durationMillis = DurationMedium1, easing = EmphasizedAccelerateEasing)
        ) +
                slideOutHorizontally(
                    animationSpec = tween(durationMillis = DurationMedium2, easing = EmphasizedAccelerateEasing)
                ) {
                    with(density) { -30.dp.roundToPx() }
                }
    }

    val popExitTransition: (Density) -> ExitTransition = { density ->
        fadeOut(
            animationSpec = tween(durationMillis = DurationMedium1, easing = EmphasizedAccelerateEasing)
        ) +
                slideOutHorizontally(
                    animationSpec = tween(durationMillis = DurationMedium2, easing = EmphasizedAccelerateEasing)
                ) {
                    with(density) { 30.dp.roundToPx() }
                }
    }
}