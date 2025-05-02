package com.madinaapps.iarmasjid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    background = backgroundLight,
    surface = surfaceLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    primaryContainer = primaryContainerLight,
    onBackground = onBackgroundLight,
    onSecondary = onSecondaryLight,
    surfaceContainer = surfaceContainerLight,
    surfaceVariant = surfaceVariantLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiary = onTertiaryLight,
    tertiary = tertiaryLight
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    background = backgroundDark,
    surface = surfaceDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    primaryContainer = primaryContainerDark,
    onBackground = onBackgroundDark,
    onSecondary = onSecondaryDark,
    surfaceContainer = surfaceContainerDark,
    surfaceVariant = surfaceVariantDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiary = onTertiaryDark,
    tertiary = tertiaryDark
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