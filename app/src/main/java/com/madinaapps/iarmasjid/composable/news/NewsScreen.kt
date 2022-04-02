package com.madinaapps.iarmasjid.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import com.madinaapps.iarmasjid.composable.news.postsList
import com.madinaapps.iarmasjid.composable.news.eventsList
import com.madinaapps.iarmasjid.ui.theme.darkGreen
import com.madinaapps.iarmasjid.ui.theme.lightGreen
import com.madinaapps.iarmasjid.viewModel.NewsViewModel


@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel()) {
    val titles = listOf("Announcements", "Events")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.announcements) {
        viewModel.didViewAnnouncements()
    }

    Column {
        TabRow(selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            backgroundColor = lightGreen,
            contentColor = darkGreen
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }
        HorizontalPager(
            count = 2,
            state = pagerState,
        ) { page ->
            if (page == 0) {
                postsList(
                    announcements = viewModel.announcements,
                    loading = viewModel.loading,
                    refreshAction = {
                        viewModel.fetchLatest()
                    }
                )
            } else {
                eventsList(events = viewModel.events,
                    loading = viewModel.loading,
                    refreshAction = {
                        viewModel.fetchLatest()
                    }
                )
            }
        }
    }
}