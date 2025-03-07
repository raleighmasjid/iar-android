package com.madinaapps.iarmasjid

import com.madinaapps.iarmasjid.utils.Utils

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    companion object {
        const val BASE_WEB_ROUTE = "webpage/{url}"
        fun webRoute(url: String): String {
            return "webpage/${Utils.encodeURL(url)}"
        }
    }

    data object Prayer : NavigationItem("prayer", R.drawable.ic_tab_prayer, "Prayer")
    data object News : NavigationItem("news", R.drawable.ic_tab_news, "News")
    data object Donate : NavigationItem("donate", R.drawable.ic_tab_donate, "Donate")
    data object More : NavigationItem("more", R.drawable.ic_tab_more, "More")
}