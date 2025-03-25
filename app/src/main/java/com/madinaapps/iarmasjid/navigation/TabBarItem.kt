package com.madinaapps.iarmasjid.navigation

import com.madinaapps.iarmasjid.R

enum class TabBarItem(val route: AppDestination, val icon: Int, val title: String, val hideTopBar: Boolean = false) {
    PRAYER(AppDestination.Prayer, R.drawable.ic_tab_prayer, "Prayer", hideTopBar = true),
    QIBLA(AppDestination.Qibla, R.drawable.ic_tab_qibla, "Qibla"),
    NEWS(AppDestination.NewsTab, R.drawable.ic_tab_news, "News"),
    DONATE(AppDestination.Donate, R.drawable.ic_tab_donate, "Donate", hideTopBar = true),
    MORE(AppDestination.More, R.drawable.ic_tab_more, "More")
}