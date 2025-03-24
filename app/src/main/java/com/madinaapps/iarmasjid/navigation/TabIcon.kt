package com.madinaapps.iarmasjid.navigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.viewModel.NewsViewModel

@Composable
fun TabIcon(item: TabBarItem, selected: Boolean, newsViewModel: NewsViewModel) {
    if (item == TabBarItem.NEWS && newsViewModel.showBadge && !selected) {
        Icon(
            painterResource(id = R.drawable.ic_tab_news_badge),
            contentDescription = item.title,
            tint = Color.Unspecified
        )
    } else {
        Icon(painterResource(id = item.icon), contentDescription = item.title)
    }
}