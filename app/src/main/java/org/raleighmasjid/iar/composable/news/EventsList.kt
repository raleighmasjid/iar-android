package org.raleighmasjid.iar.composable.news

import androidx.compose.foundation.ExperimentalFoundationApi
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
import org.raleighmasjid.iar.model.json.Event
import org.raleighmasjid.iar.ui.theme.DarkColorMode
import org.raleighmasjid.iar.ui.theme.LightColorMode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun eventsList(events: Map<String, List<Event>>, loading: Boolean, refreshAction: () -> Unit) {
    val colors = if (MaterialTheme.colors.isLight) LightColorMode() else DarkColorMode()
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = loading), onRefresh = {
        refreshAction()
    }) {
        LazyColumn {
            events.forEach { (date, eventsForDate) ->
                stickyHeader {
                    dateHeader(date)
                }
                items(eventsForDate) { event ->
                    eventRow(event)
                    Divider(
                        color = colors.dividerColor(),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(start = 32.dp, end = 16.dp)
                    )
                }
            }
        }
    }
}