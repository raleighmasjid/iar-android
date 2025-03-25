package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.navigation.AppDestination
import com.madinaapps.iarmasjid.viewModel.NewsViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel(), paddingValues: PaddingValues, navigateToWeb: (AppDestination.Web) -> Unit) {
    val announcements = viewModel.announcements

    val pullRefreshState = rememberPullRefreshState(viewModel.loading, {
        viewModel.loadData(forceRefresh = true)
    })

    LaunchedEffect(viewModel.announcements) {
        viewModel.didViewAnnouncements()
    }

    Box(Modifier.pullRefresh(pullRefreshState)) {
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
        PullRefreshIndicator(viewModel.loading, pullRefreshState, Modifier.align(Alignment.TopCenter), backgroundColor = MaterialTheme.colorScheme.primary)
    }
}