package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.viewModel.PrayerCountdownViewModel
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrayerScreen(viewModel: PrayerTimesViewModel = hiltViewModel()) {
    val prayerPagerState = rememberPagerState()
    val khutbaPagerState = rememberPagerState()

    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel.didResume) {
        if (viewModel.didResume) {
            scope.launch {
                prayerPagerState.scrollToPage(0)
                khutbaPagerState.scrollToPage(0)
            }
            viewModel.didResume = false
        }
    }

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = viewModel.loading), onRefresh = {
        viewModel.fetchLatest()
    }) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
        ) {
            PrayerCountdown(viewModel = PrayerCountdownViewModel(viewModel.upcoming))
            PrayerHeader(viewModel.prayerDays, prayerPagerState)
            PrayerTimesView(
                prayerDays = viewModel.prayerDays,
                pagerState = prayerPagerState,
                dataStoreManager = viewModel.dataStoreManager
            )
            FridayScheduleView(viewModel.fridayPrayers, khutbaPagerState)

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
}