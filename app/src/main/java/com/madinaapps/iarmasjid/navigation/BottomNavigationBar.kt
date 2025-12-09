package com.madinaapps.iarmasjid.navigation

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madinaapps.iarmasjid.viewModel.NewsViewModel

@Composable
fun BottomNavigationBar(navController: NavController, newsViewModel: NewsViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(
        modifier = Modifier.defaultMinSize(minHeight = 64.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        TabBarItem.entries.forEach { tabItem ->
            val isSelected = navBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(tabItem.route::class) } == true

            NavigationBarItem(
                modifier = Modifier.defaultMinSize(minHeight = 64.dp),
                icon = {
                    TabIcon(tabItem, isSelected, newsViewModel)
                },
                label = {
                    Text(tabItem.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    if (!isSelected) {
                        navController.navigate(tabItem.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        val currentDestination = navBackStackEntry?.destination
                        var isAtRoot = currentDestination?.hasRoute(tabItem.route::class) == true
                        if (tabItem == TabBarItem.NEWS) {
                            // Check if the current destination's PARENT is the NewsTab graph
                            val parentGraphRoute = currentDestination?.parent?.hasRoute(AppDestination.NewsTab::class) == true
                            // The root is the start destination of the graph, which is AppDestination.News
                            val isAtGraphStart = currentDestination?.hasRoute(AppDestination.News::class) == true
                            isAtRoot = parentGraphRoute && isAtGraphStart
                        }

                        if (!isAtRoot) {
                            navController.navigate(tabItem.route) {
                                popUpTo(tabItem.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                selected = isSelected
            )
        }
    }
}

