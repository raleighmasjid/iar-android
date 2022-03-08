package org.raleighmasjid.iar.composable.news

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.raleighmasjid.iar.model.json.Announcement

@Composable
fun announcementsList(announcements: List<Announcement>, loading: Boolean, refreshAction: () -> Unit) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = loading), onRefresh = {
        refreshAction()
    }) {
        LazyColumn {
            items(announcements) { announcement ->
                announcementRow(announcement)
                Divider(
                    color = Color.LightGray,
                    thickness = 0.5.dp,
                )
            }
        }
    }
}