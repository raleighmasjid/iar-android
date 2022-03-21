package org.raleighmasjid.iar.composable.news

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.raleighmasjid.iar.model.json.Announcements
import org.raleighmasjid.iar.ui.theme.DarkColorMode
import org.raleighmasjid.iar.ui.theme.LightColorMode

@Composable
fun announcementsList(
    announcements: Announcements?,
    loading: Boolean,
    refreshAction: () -> Unit,
) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = loading), onRefresh = {
        refreshAction()
    }) {
        LazyColumn {
            if (announcements?.special != null) {
                item {
                    specialHeader(announcements.special)
                }
            }

            if (announcements?.featured != null) {
                item {
                    announcementRow(announcements.featured)
                    announcementsDivider()
                }
            }

            items(announcements?.posts ?: emptyList()) { post ->
                announcementRow(post)
                announcementsDivider()
            }
        }
    }
}

@Composable
fun announcementsDivider() {
    val colors = if (MaterialTheme.colors.isLight) LightColorMode() else DarkColorMode()
    Divider(color = colors.dividerColor(),
        thickness = 0.5.dp,
        modifier = Modifier.padding(horizontal = 16.dp))
}