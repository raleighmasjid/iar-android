package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@Composable
fun PrayerTimesScreen(viewModel: PrayerTimesViewModel) {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Prayer Times",
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .padding(top = 20.dp, bottom = 6.dp),
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold
        )
        PrayerTimesHeader(viewModel.prayerDay, viewModel.upcoming, viewModel.timeRemaining)
        PrayerTimesList(viewModel.prayerDay, viewModel.dataStoreManager)

        if (viewModel.error) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.error = false
                },
                title = {
                    Text("Error")
                },
                text = {
                    Text("Unable to load prayer times")
                },
                buttons = {
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                        Button(onClick = { viewModel.error = false }) {
                            Text("Dismiss")
                        }
                        Button(modifier = Modifier.padding(start = 10.dp, end = 5.dp), onClick = {
                            viewModel.error = false
                            viewModel.loadPrayerTimes()
                        }) {
                            Text("Retry")
                        }
                    }
                }
            )
        }
    }
}