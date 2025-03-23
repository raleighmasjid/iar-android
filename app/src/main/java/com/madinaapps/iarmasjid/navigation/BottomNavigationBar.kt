package com.madinaapps.iarmasjid.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val items = listOf(NavigationItem.Prayer, NavigationItem.News, NavigationItem.Donate, NavigationItem.More)

    NavigationBar {
        items.forEach {
            NavigationBarItem(
                icon = {
                    Icon(painterResource(id = it.icon), contentDescription = it.title)
                },
                label = {
                    Text(it.title)
                },
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

