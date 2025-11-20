package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.utils.Countdown
import com.madinaapps.iarmasjid.utils.pxToDp
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun PrayerScreen(viewModel: PrayerTimesViewModel = hiltViewModel(), paddingValues: PaddingValues) {
    val prayerPagerState = rememberPagerState(pageCount = { max(1, viewModel.prayerDays.count()) })
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var headerSize by remember { mutableIntStateOf(0) }
    var smallHeaderSize by remember { mutableIntStateOf(0) }
    var timeRemaining by remember { mutableLongStateOf(viewModel.upcoming?.timeRemaining() ?: -1) }

    LaunchedEffect(viewModel.upcoming) {
        timeRemaining = viewModel.upcoming?.timeRemaining() ?: -1
    }

    Countdown(viewModel.upcoming?.adhan?.time ?: 0) {
        viewModel.updateNextPrayer()
        timeRemaining = it
    }

    fun headerOpacity(): Float {
        val position = scrollState.value - paddingValues.calculateTopPadding().value
        return if (position <= 265) {
            0.0f
        } else if (position >= 290) {
            1.0f
        } else {
            (position - 265) / 25
        }
    }

    fun countdownOpacity(): Float {
        val position = scrollState.value - paddingValues.calculateTopPadding().value
        return if (position <= 240) {
            1.0f
        } else if (position >= 290) {
            0.0f
        } else {
            1 - ((position - 240) / 50)
        }
    }

    LaunchedEffect(viewModel.didResume) {
        if (viewModel.didResume) {
            scope.launch {
                prayerPagerState.scrollToPage(0)
            }
            viewModel.didResume = false
        }
    }

    Box(
        modifier = Modifier
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Image(
            painter = painterResource(id = R.drawable.prayer_header),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height((40.dp + headerSize.pxToDp() + paddingValues.calculateTopPadding()) - scrollState.value.pxToDp())
        )

        Column(modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
        ) {
            Box(Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    headerSize = it.height
                }
            ) {
                Box(modifier = Modifier.alpha(countdownOpacity())) {
                    PrayerCountdown(viewModel.upcoming, timeRemaining = timeRemaining)
                }
                if (viewModel.loading) {
                    Row(modifier = Modifier.padding(end = 30.dp).align(Alignment.TopEnd)) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(25.dp),
                            color = Color.White
                        )
                    }

                }
            }

            Box(modifier = Modifier
                .shadow(elevation = 24.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
            ) {
                Column(modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                ) {
                    PrayerHeader(viewModel.prayerDays, prayerPagerState)
                    PrayerTimesView(
                        prayerDays = viewModel.prayerDays,
                        current = viewModel.current,
                        pagerState = prayerPagerState
                    )
                }
            }

            Spacer(modifier = Modifier.size(32.dp))

            if (viewModel.fridayPrayers.isNotEmpty()) {
                FridayScheduleView(viewModel.fridayPrayers)
            }

            if (viewModel.error) {
                PrayerTimesLoadError(
                    dismissAction = {
                        viewModel.error = false
                    },
                    retryAction = {
                        viewModel.error = false
                        viewModel.loadData()
                    })
            }
        }

        Box(modifier = Modifier.alpha(headerOpacity())) {
            Image(
                painter = painterResource(id = R.drawable.prayer_header),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(smallHeaderSize.pxToDp() + paddingValues.calculateTopPadding())
            )
            Box(modifier = Modifier.padding(paddingValues).onSizeChanged { smallHeaderSize = it.height }) {
                SmallPrayerCountdown(
                    upcoming = viewModel.upcoming,
                    timeRemaining = timeRemaining
                )
            }
        }
    }

}