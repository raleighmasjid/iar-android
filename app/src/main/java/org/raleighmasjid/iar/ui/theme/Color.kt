package org.raleighmasjid.iar.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val statusBarColor = Color(0xFF124016)
val Teal200 = Color(0xFF03DAC5)
val darkGreen = Color(0xFF1B5E20)
val lightGreen = Color(0xFFEDF9EE)
val secondaryTextColor = Color(0xFF4B5555)
val tertiaryTextcolor = Color(0xFF8E8E93)
val rowColor = Color(0xFFF2F2F7)

abstract class ColorMode {
    abstract fun hijriTextColor(): Color
    abstract fun prayerColor(): Color
    abstract fun prevPrayerColor(): Color
    abstract fun nextPrayerColor(): Color
    abstract fun currentPrayerBorderColor(): Color
    abstract fun badgeBackgroundColor(): Color
    abstract fun currentPrayerBackgroundColor(): Color
    abstract fun prayerBorderColor(): Color
    abstract fun prevButtonTint(): Color
    abstract fun buttonTint(): Color
    abstract fun bottomNavBackgroundColor(): Color
    abstract fun bottomNavContentColor(): Color
    abstract fun bottomNavSelectedContentColor(): Color
    abstract fun bottomNavUnselectedContentColor(): Color
    abstract fun tabBackgroundColor(): Color
    abstract fun tabContentColor(): Color
    abstract fun specialHeaderBackgroundColor(): Color
    abstract fun specialHeaderTitleColor(): Color
    abstract fun dividerColor(): Color
    abstract fun postTextColor(): Color
}

class LightColorMode : ColorMode() {
    override fun hijriTextColor(): Color {
        return Color(0xFF4B5555)
    }

    override fun prayerColor(): Color {
        return Color.Black
    }

    override fun prevPrayerColor(): Color {
        return Color(0xFFAEAEB2)
    }

    override fun nextPrayerColor(): Color {
        return Color(0xFF4B5555)
    }

    override fun currentPrayerBorderColor(): Color {
        return darkGreen
    }

    override fun badgeBackgroundColor(): Color {
        return Color(0x164B5555)
    }

    override fun currentPrayerBackgroundColor(): Color {
        return Color(0xFFDCF9D7)
    }

    override fun prayerBorderColor(): Color {
        return Color(0xFFBCBEC0)
    }

    override fun prevButtonTint(): Color {
        return Color(0x801B5E20)
    }

    override fun buttonTint(): Color {
        return darkGreen
    }

    override fun bottomNavBackgroundColor(): Color {
        return darkGreen
    }

    override fun bottomNavContentColor(): Color {
        return Color.White
    }

    override fun bottomNavSelectedContentColor(): Color {
        return Color.White
    }

    override fun bottomNavUnselectedContentColor(): Color {
        return Color.White.copy(0.4f)
    }

    override fun tabBackgroundColor(): Color {
        return lightGreen
    }

    override fun tabContentColor(): Color {
        return darkGreen
    }

    override fun specialHeaderBackgroundColor(): Color {
        return Color(0xFFDCF9D7)
    }

    override fun specialHeaderTitleColor(): Color {
        return darkGreen
    }

    override fun dividerColor(): Color {
        return Color(0xFFAEAEB2)
    }

    override fun postTextColor(): Color {
        return Color(0xFF4B5555)
    }
}

class DarkColorMode : ColorMode() {
    override fun hijriTextColor(): Color {
        return Color(0xFF8E8E93)
    }

    override fun prayerColor(): Color {
        return Color.White
    }

    override fun prevPrayerColor(): Color {
        return Color(0xFF636366)
    }


    override fun nextPrayerColor(): Color {
        return Color(0xFF8E8E93)
    }

    override fun currentPrayerBorderColor(): Color {
        return Color(0xFF2B9733)
    }

    override fun badgeBackgroundColor(): Color {
        return Color(0x26FFFFFF)
    }

    override fun currentPrayerBackgroundColor(): Color {
        return Color(0x592B9733)
    }

    override fun prayerBorderColor(): Color {
        return Color(0xFF636366)
    }

    override fun prevButtonTint(): Color {
        return Color(0x802B9733)
    }

    override fun buttonTint(): Color {
        return Color(0xFF2B9733)
    }

    override fun bottomNavBackgroundColor(): Color {
        return Color(0xFF1C1C1E)
    }

    override fun bottomNavContentColor(): Color {
        return Color.White
    }

    override fun bottomNavSelectedContentColor(): Color {
        return Color(0xFF2B9733)
    }

    override fun bottomNavUnselectedContentColor(): Color {
        return Color.White.copy(0.4f)
    }

    override fun tabBackgroundColor(): Color {
        return Color(0x592B9733)
    }

    override fun tabContentColor(): Color {
        return Color.White
    }

    override fun specialHeaderBackgroundColor(): Color {
        return Color(0x592B9733)
    }

    override fun specialHeaderTitleColor(): Color {
        return Color(0xFF2B9733)
    }

    override fun dividerColor(): Color {
        return Color(0xFF636366)
    }

    override fun postTextColor(): Color {
        return Color(0xFFC7C7CC)
    }
}
