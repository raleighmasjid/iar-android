package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import org.raleighmasjid.iar.composable.PrayerTimesScreen
import org.raleighmasjid.iar.ui.theme.IARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.darkGreen)
            IARTheme {
                PrayerTimesScreen()
            }
        }
    }
}

