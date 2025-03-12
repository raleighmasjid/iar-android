package com.madinaapps.iarmasjid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    secondary = secondaryLight,
    background = Color.White,
    surface = surfaceLight,
    onBackground = Color.Black,
    onSecondary = onSecondaryLight,
    onTertiary = onTertiaryLight
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    secondary = secondaryDark,
    background = Color.Black,
    surface = surfaceDark,
    onBackground = Color.White,
    onSecondary = onSecondaryDark,
    onTertiary = onTertiaryDark
)

@Composable
fun IARTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}