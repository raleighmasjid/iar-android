package org.raleighmasjid.iar.composable.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.raleighmasjid.iar.model.json.Event
import org.raleighmasjid.iar.ui.theme.dividerColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun eventsList(events: Map<String, List<Event>>, loading: Boolean, refreshAction: () -> Unit) {
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
                        color = dividerColor,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}