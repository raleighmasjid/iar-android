package com.madinaapps.iarmasjid.composable.news

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.madinaapps.iarmasjid.model.json.Event
import com.madinaapps.iarmasjid.ui.theme.divider
import com.madinaapps.iarmasjid.utils.formatToDay
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun EventsList(events: Map<LocalDate, List<Event>>, loading: Boolean, refreshAction: () -> Unit) {
    val pullRefreshState = rememberPullRefreshState(loading, { refreshAction() })

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn {
            events.keys.sorted().forEach { date ->
                stickyHeader {
                    DateHeader(date.formatToDay())
                }
                val dateEvents = events[date]?.sortedBy { it.start } ?: emptyList()
                items(dateEvents) { event ->
                    EventRow(event)
                    Divider(
                        color = MaterialTheme.colors.divider,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(start = 32.dp, end = 16.dp)
                    )
                }
            }
        }

        PullRefreshIndicator(loading, pullRefreshState, Modifier.align(Alignment.TopCenter), backgroundColor = MaterialTheme.colors.primary)
    }
}