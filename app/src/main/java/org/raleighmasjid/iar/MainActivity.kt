package org.raleighmasjid.iar

import android.os.Bundle
import android.text.format.DateUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.api.ApiClient
import org.raleighmasjid.iar.model.Prayer
import org.raleighmasjid.iar.model.PrayerDay
import org.raleighmasjid.iar.ui.theme.IARTheme
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.utils.formatToDay
import org.raleighmasjid.iar.utils.formatToTime
import java.util.*

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

@Composable
fun PrayerTimesScreen(viewModel: PrayerTimesViewModel = viewModel()) {
    val prayerTimes = viewModel.prayerTimes

    Column {
        Text(
            "Prayer Times",
            modifier = Modifier.padding(horizontal = 18.dp).padding(top = 20.dp, bottom = 6.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )
        PrayerTimesHeader(prayerTimes)
        if (prayerTimes != null) {
            PrayerTimesList(prayerTimes)
        } else {
            Text("Loading...", modifier = Modifier.padding(20.dp))
        }
    }
}

@Composable fun PrayerTimesHeader(prayerDay: PrayerDay?) {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        if (prayerDay != null) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(prayerDay.date.formatToDay())
                Text(prayerDay.hijri.fomatted(), fontStyle = FontStyle.Italic)
            }
        }
        Row(modifier = Modifier
            .background(darkGreen)
            .padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text("Prayer",
                modifier = Modifier.weight(1f, true),
                fontSize = 16.sp,
                color = Color.White)
            Text("Adhan",
                modifier = Modifier.weight(1f, true),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White)
            Text("Iqamah",
                modifier = Modifier.weight(1f, true),
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                color = Color.White)
        }
    }
}

@Composable fun PrayerTimesList(prayerDay: PrayerDay) {
    val currentPrayer = prayerDay.currentPrayer()
    Column {
        Prayer.values().forEach { prayer ->
            PrayerTimeRow(
                prayer = prayer,
                adhan = prayerDay.adhanTime(prayer),
                iqamah = prayerDay.iqamahTime(prayer),
                current = currentPrayer == prayer
            )
        }
    }
}

@Composable fun PrayerTimeRow(prayer: Prayer, adhan: Date, iqamah: Date?, current: Boolean) {
    val bgAlpha: Float = if (current) 0.1f else 0f
    Row(modifier = Modifier
        .background(Color.Green.copy(alpha = bgAlpha))
        .padding(horizontal = 20.dp, vertical = 8.dp)) {
        Box(modifier = Modifier.weight(1f, true)) {
            if (current) {
                Text("\u2022",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset((-12).dp, 2.dp))
            }
            Text(prayer.title(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold)
        }

        Text(adhan.formatToTime(),
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
        Text(iqamah?.formatToTime() ?: "",
            modifier = Modifier.weight(1f, true),
            textAlign = TextAlign.End,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
    }
}

class PrayerTimesViewModel : ViewModel() {
    var prayerTimes by mutableStateOf<PrayerDay?>(null)

    init {
        loadPrayerTimes()
    }

    private fun loadPrayerTimes() {
        viewModelScope.launch {
            val response = ApiClient.getPrayerTimes()
            prayerTimes = response.first { DateUtils.isToday(it.date.time) }
        }
    }
}
