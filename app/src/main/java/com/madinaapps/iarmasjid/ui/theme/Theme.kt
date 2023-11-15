package com.madinaapps.iarmasjid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val lightColorPalette = lightColors(
    primary = primaryLight,
    background = Color.White,
    surface = surfaceLight,
    onBackground = Color.Black,
    onSurface = onSurfaceLight,
)

private val darkColorPalette = darkColors(
        primary = primaryDark,
        background = Color.Black,
        surface = surfaceDark,
        onBackground = Color.White,
        onSurface = onSurfaceDark,
)

@Composable
fun IARTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}