package com.madinaapps.iarmasjid.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madinaapps.iarmasjid.viewModel.NewsViewModel

@Composable
fun BottomNavigationBar(navController: NavController, newsViewModel: NewsViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        TabBarItem.entries.forEach { tabItem ->
            val isSelected = navBackStackEntry?.destination?.hasRoute(tabItem.route::class) == true

            NavigationBarItem(
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
                    navController.navigate(tabItem.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                    }
                },
                selected = isSelected
            )
        }
    }
}

