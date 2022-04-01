package com.madinaapps.iarmasjid.composable.news

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
import com.madinaapps.iarmasjid.model.json.Event
import com.madinaapps.iarmasjid.ui.theme.dividerColor
import com.madinaapps.iarmasjid.utils.formatToDay
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun eventsList(events: Map<LocalDate, List<Event>>, loading: Boolean, refreshAction: () -> Unit) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = loading), onRefresh = {
        refreshAction()
    }) {
        LazyColumn {
            events.keys.sorted().forEach { date ->
                stickyHeader {
                    dateHeader(date.formatToDay())
                }
                val events = events[date]?.sortedBy { it.start } ?: emptyList()
                items(events) { event ->
                    eventRow(event)
                    Divider(
                        color = dividerColor,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(start = 32.dp, end = 16.dp)
                    )
                }
            }
        }
    }
}