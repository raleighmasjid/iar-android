package com.madinaapps.iarmasjid.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF1B5E20)
val primaryDark = Color(0xFF2B9733)
val surfaceLight = Color(0x1A1B5E20)
val surfaceDark = Color(0x402B9733)
val onSurfaceLight = Color(0xFF4B5555)
val onSurfaceDark = Color(0xFFC7C7CC)

val Colors.currentPrayerBackground: Color
    get() = if (isLight) Color(0xFFDCF9D7) else Color(0x592B9733)

val Colors.prayerBorder:  Color
    get() = if (isLight) Color(0xFFBCBEC0) else Color(0xFF636366)

val Colors.currentPrayerBorder:  Color
    get() = if (isLight) Color(0xFF1B5E20) else Color(0xFF2B9733)

val Colors.badgeBackground: Color
    get() = if (isLight) Color(0x1A4B5555) else Color(0x33FFFFFF)

val Colors.secondaryText: Color
    get() = if (isLight) Color(0xFF4B5555) else Color(0xFFC7C7CC)

val Colors.tertiaryText: Color
    get() = if (isLight) Color(0xFF8E8E93) else Color(0xFF8E8E93)

val Colors.rowColor: Color
    get() = if (isLight) Color(0xFFF2F2F7) else Color(0xFF1B1B1D)

val Colors.divider: Color
    get() = if (isLight) Color(0xFFAEAEB2) else Color(0xCCAEAEB2)

val darkGreen = Color(0xFF1B5E20)
