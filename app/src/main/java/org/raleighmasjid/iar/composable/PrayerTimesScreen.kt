package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@Composable
fun PrayerTimesScreen(viewModel: PrayerTimesViewModel) {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(title = { Text(text = "Prayer Times") }, backgroundColor = darkGreen)
        PrayerTimesHeader(viewModel.prayerDay, viewModel.upcoming)
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
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()) {
                        Button(onClick = { viewModel.error = false }) {
                            Text("Dismiss")
                        }
                        Button(modifier = Modifier.padding(start = 10.dp, end = 5.dp), onClick = {
                            viewModel.error = false
                            viewModel.fetchLatest()
                        }) {
                            Text("Retry")
                        }
                    }
                }
            )
        }
    }
}