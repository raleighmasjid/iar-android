package org.raleighmasjid.iar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = darkGreen,
    primaryVariant = lightGreen,
    secondary = Teal200,
)

private val LightColorPalette = lightColors(
    primary = darkGreen,
    primaryVariant = lightGreen,
    secondary = Teal200,


    /* Other default colors to override

onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/
)

@Composable
fun IARTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val darkTheme = darkTheme

    val colors = if (darkTheme)
        DarkColorPalette
    else
        DarkColorPalette


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}