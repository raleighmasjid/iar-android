package com.madinaapps.iarmasjid.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madinaapps.iarmasjid.NavigationItem
import com.madinaapps.iarmasjid.ui.theme.bottomNavText

@Composable
fun BottomNavigationBar(navController: NavController, showBadge: Boolean) {
    val items = listOf(
        NavigationItem.Prayer,
        NavigationItem.News,
        NavigationItem.Donate,
        NavigationItem.More
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.bottomNavText
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    BadgedBox(badge = {
                        if (item == NavigationItem.News && showBadge) {
                            Badge(modifier = Modifier.size(12.dp, 12.dp)) {
                                Text(text = "")
                            }
                        }
                    }) {
                        Icon(painterResource(id = item.icon), contentDescription = item.title)
                    }
                },
                label = { Text(text = item.title) },
                selectedContentColor = MaterialTheme.colors.bottomNavText,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
}
