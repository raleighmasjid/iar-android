package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madinaapps.iarmasjid.navigation.AppDestination
import com.madinaapps.iarmasjid.viewModel.NewsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel(), paddingValues: PaddingValues, navigateToWeb: (AppDestination.Web) -> Unit) {
    val announcements = viewModel.announcements
    val state = rememberPullToRefreshState()

    LaunchedEffect(viewModel.announcements) {
        viewModel.didViewAnnouncements()
    }

    PullToRefreshBox(
        isRefreshing = viewModel.loading,
        onRefresh = { viewModel.loadData(forceRefresh = true) },
        state = state,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = state,
                isRefreshing = viewModel.loading,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = MaterialTheme.colorScheme.primary,
                color = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            if (announcements?.special != null) {
                item {
                    SpecialHeader(announcements.special, navigateToWeb)
                }
            }

            if (announcements?.featured != null) {
                item {
                    PostRow(announcements.featured, navigateToWeb)
                    AnnouncementsDivider()
                }
            }

            items(announcements?.posts ?: emptyList()) { post ->
                PostRow(post, navigateToWeb)
                AnnouncementsDivider()
            }
        }
    }
}