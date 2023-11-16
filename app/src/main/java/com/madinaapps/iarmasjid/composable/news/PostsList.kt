package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.madinaapps.iarmasjid.model.json.Announcements
import com.madinaapps.iarmasjid.ui.theme.divider

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostsList(
    announcements: Announcements?,
    loading: Boolean,
    refreshAction: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(loading, { refreshAction() })

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn {
            if (announcements?.special != null) {
                item {
                    SpecialHeader(announcements.special)
                }
            }

            if (announcements?.featured != null) {
                item {
                    PostRow(announcements.featured)
                    AnnouncementsDivider()
                }
            }

            items(announcements?.posts ?: emptyList()) { post ->
                PostRow(post)
                AnnouncementsDivider()
            }
        }
        PullRefreshIndicator(loading, pullRefreshState, Modifier.align(Alignment.TopCenter), backgroundColor = MaterialTheme.colors.primary)
    }
}

@Composable
fun AnnouncementsDivider() {
    Divider(color = MaterialTheme.colors.divider,
        thickness = 0.5.dp,
        modifier = Modifier.padding(horizontal = 16.dp))
}