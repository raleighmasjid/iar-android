package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.ui.theme.AppColors
import com.madinaapps.iarmasjid.viewModel.PrayerCountdownViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(viewModel: PrayerTimesViewModel = hiltViewModel(), paddingValues: PaddingValues) {
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

    Box {
        Column(modifier = Modifier
            .height(with(LocalDensity.current) { WindowInsets.statusBars.getTop(this).toDp() })
            .fillMaxWidth()
            .background(AppColors.primaryFixed)) {
        }
        Column(modifier = Modifier

            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(paddingValues)

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
                BasicAlertDialog(
                    onDismissRequest = {
                        viewModel.error = false
                    }
                ) {
                    Surface(modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults. TonalElevation
                    ) {
                        Column(modifier = Modifier. padding(16.dp)) {
                            Text("Error",
                                modifier = Modifier
                                    .padding(bottom = 16.dp) // space between title and content
                                    .semantics { heading() },
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text("Unable to load prayer times",
                                modifier = Modifier
                                    .padding(bottom = 24.dp))

                            Row(modifier = Modifier.align(Alignment.End)) {
                                TextButton(
                                    onClick = { viewModel.error = false }
                                ) {
                                    Text("Dismiss")
                                }
                                TextButton(
                                    onClick = {
                                        viewModel.error = false
                                        viewModel.loadData()
                                    }
                                ) {
                                    Text("Retry")
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}