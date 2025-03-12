package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.viewModel.NewsViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel()) {
    val titles = listOf("Announcements", "Events")
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.announcements) {
        viewModel.didViewAnnouncements()
    }

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                PostsList(
                    announcements = viewModel.announcements,
                    loading = viewModel.loading,
                    refreshAction = {
                        viewModel.loadData(forceRefresh = true)
                    }
                )
            } else {
                EventsList(events = viewModel.events,
                    loading = viewModel.loading,
                    refreshAction = {
                        viewModel.loadData(forceRefresh = true)
                    }
                )
            }
        }
    }
}