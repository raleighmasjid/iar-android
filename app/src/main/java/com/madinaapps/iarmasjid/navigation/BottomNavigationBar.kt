package com.madinaapps.iarmasjid.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.viewModel.NewsViewModel

@Composable
fun TabIcon(item: NavigationItem, newsViewModel: NewsViewModel) {
    val darkMode = isSystemInDarkTheme()

    if (item.route == NavigationItem.News.route && newsViewModel.showBadge) {
        if (darkMode) {
            Icon(painterResource(id = R.drawable.ic_tab_news_badge_dark), contentDescription = item.title, tint = Color.Unspecified)
        } else {
            Icon(painterResource(id = R.drawable.ic_tab_news_badge), contentDescription = item.title, tint = Color.Unspecified)
        }
    } else {
        Icon(painterResource(id = item.icon), contentDescription = item.title)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, newsViewModel: NewsViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val items = listOf(NavigationItem.Prayer, NavigationItem.Qibla, NavigationItem.News, NavigationItem.Donate, NavigationItem.More)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach {
            NavigationBarItem(
                icon = {
                    TabIcon(it, newsViewModel)
                },
                label = {
                    Text(it.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    navController.navigate(it.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                    }
                },
                selected = navBackStackEntry?.destination?.route?.startsWith(it.route) ?: false
            )
        }
    }
}

