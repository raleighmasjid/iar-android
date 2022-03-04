package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.ui.theme.darkGreen
import org.raleighmasjid.iar.ui.theme.lightGreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewsScreen() {
    val titles = listOf("Announcements", "Events")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
    ) {

        TabRow(selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            backgroundColor = lightGreen,
            contentColor = darkGreen
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }
        HorizontalPager(
            count = 2,
            state = pagerState,
        ) { page ->
            if (page == 0) {
                Text(
                    "Announcements",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            } else {
                Text(
                    "Events",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    NewsScreen()
}