package org.raleighmasjid.iar

import org.raleighmasjid.iar.utils.Utils

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    companion object {
        const val baseWebRoute = "webpage/{url}"
        fun webRoute(url: String): String {
            return "webpage/${Utils.encodeURL(url)}"
        }
    }

    object Prayer : NavigationItem("prayer", R.drawable.ic_tab_prayer, "Prayer")
    object News : NavigationItem("news", R.drawable.ic_tab_news, "News")
    object Donate : NavigationItem("donate", R.drawable.ic_tab_donate, "Donate")
    object More : NavigationItem("more", R.drawable.ic_tab_more, "More")
}