package com.madinaapps.iarmasjid.navigation

import androidx.navigation.NavBackStackEntry
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.utils.Utils

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    companion object {
        const val BASE_WEB_ROUTE = "news/webpage/{url}/{title}"
        fun webRoute(url: String, title: String): String {
            return "news/webpage/${Utils.encodeURL(url)}/${Utils.encodeURL(title)}"
        }

        fun routeTitle(navBackStackEntry: NavBackStackEntry?): String {
            if (navBackStackEntry?.destination?.route == BASE_WEB_ROUTE) {
                val encodedTitle = navBackStackEntry.arguments?.getString("title")
                if (encodedTitle != null) {
                    return Utils.decodeURL(encodedTitle)
                }
                val encodedUrl = navBackStackEntry.arguments?.getString("url")
                if (encodedUrl != null) {
                    return Utils.decodeURL(encodedUrl)
                }
                return ""
            }
            return when (navBackStackEntry?.destination?.route) {
                Prayer.route -> "Prayer"
                Qibla.route -> "Qibla"
                News.route -> "News"
                Donate.route -> "Donate"
                More.route -> "More"
                else -> ""
            }
        }
    }

    data object Prayer : NavigationItem("prayer", R.drawable.ic_tab_prayer, "Prayer")
    data object Qibla : NavigationItem("qibla", R.drawable.ic_tab_qibla, "Qibla")
    data object News : NavigationItem("news", R.drawable.ic_tab_news, "News")
    data object Donate : NavigationItem("donate", R.drawable.ic_tab_donate, "Donate")
    data object More : NavigationItem("more", R.drawable.ic_tab_more, "More")
}