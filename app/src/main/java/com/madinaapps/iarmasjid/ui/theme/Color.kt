package com.madinaapps.iarmasjid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF1B5E20)
val primaryDark = Color(0xFF2B9733)

val secondaryLight = Color(0x1A1B5E20)
val secondaryDark = Color(0x402B9733)

val surfaceLight = Color(0xFF1B5E20)
val surfaceDark = Color(0xFF030303)

val onSecondaryLight = Color(0xFF4B5555)
val onSecondaryDark = Color(0xFFC7C7CC)

val onTertiaryLight = Color(0xFF8E8E93)
val onTertiaryDark = Color(0xFF8E8E93)

object AppColors {
    val currentPrayerBackground: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFDCF9D7) else Color(0x592B9733)

    val prayerBorder:  Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFBCBEC0) else Color(0xFF636366)

    val currentPrayerBorder:  Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF1B5E20) else Color(0xFF2B9733)

    val badgeBackground: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0x1A4B5555) else Color(0x33FFFFFF)

    val rowColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFF2F2F7) else Color(0xFF1B1B1D)

    val divider: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFAEAEB2) else Color(0xCCAEAEB2)

    val bottomNavText: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF2B9733)

    val darkGreen = Color(0xFF1B5E20)
}
