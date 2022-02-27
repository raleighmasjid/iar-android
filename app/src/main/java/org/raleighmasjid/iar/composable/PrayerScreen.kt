package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.viewModel.PrayerCountdownViewModel
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PrayerScreen(viewModel: PrayerTimesViewModel) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                scope.launch {
                    pagerState.scrollToPage(0)
                }
            }
            else -> { }
        }
    }

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(title = { Text(text = "Prayer Times") }, backgroundColor = darkGreen)
        PrayerCountdown(viewModel = PrayerCountdownViewModel(viewModel.upcoming))
        PrayerHeader(viewModel.prayerDays, pagerState)
        PrayerTimesView(
            prayerDays = viewModel.prayerDays,
            pagerState = pagerState,
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