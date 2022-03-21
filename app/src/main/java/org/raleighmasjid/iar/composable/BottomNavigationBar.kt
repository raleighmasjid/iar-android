package org.raleighmasjid.iar

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.raleighmasjid.iar.ui.theme.DarkColorMode
import org.raleighmasjid.iar.ui.theme.LightColorMode

@Composable
fun BottomNavigationBar(navController: NavController, showBadge: Boolean) {
    val colors = if (MaterialTheme.colors.isLight) LightColorMode() else DarkColorMode()
    val items = listOf(
        NavigationItem.Prayer,
        NavigationItem.News,
        NavigationItem.Donate,
        NavigationItem.More
    )
    BottomNavigation(
        backgroundColor = colors.bottomNavBackgroundColor(),
        contentColor =colors.bottomNavContentColor()
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
                selectedContentColor = colors.bottomNavSelectedContentColor(),
                unselectedContentColor = colors.bottomNavUnselectedContentColor(),
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
