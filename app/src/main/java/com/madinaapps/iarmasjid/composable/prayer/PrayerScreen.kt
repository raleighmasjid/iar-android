package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerCountdownViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerScreen(viewModel: PrayerTimesViewModel = hiltViewModel()) {
    val prayerPagerState = rememberPagerState(pageCount = { viewModel.prayerDays.count() })

    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel.didResume) {
        if (viewModel.didResume) {
            scope.launch {
                prayerPagerState.scrollToPage(0)
            }
            viewModel.didResume = false
        }
    }

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
    ) {
        Box(Modifier.fillMaxWidth()) {
            PrayerCountdown(viewModel = PrayerCountdownViewModel(viewModel.upcoming))
            if (viewModel.loading) {
                Row(modifier = Modifier.padding(end = 30.dp).align(Alignment.CenterEnd)) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp)
                    )
                }

            }
        }
        PrayerHeader(viewModel.prayerDays, prayerPagerState)
        PrayerTimesView(
            prayerDays = viewModel.prayerDays,
            pagerState = prayerPagerState,
            dataStoreManager = viewModel.dataStoreManager
        )
        FridayScheduleView(viewModel.fridayPrayers)

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
                            viewModel.loadData()
                        }) {
                            Text("Retry")
                        }
                    }
                }
            )
        }
    }
}