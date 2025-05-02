package com.madinaapps.iarmasjid.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.madinaapps.iarmasjid.composable.qibla.LocationCard
import com.madinaapps.iarmasjid.composable.web.WebActions
import com.madinaapps.iarmasjid.composable.web.WebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavController, webState: WebViewState, scrollBehavior: TopAppBarScrollBehavior) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    fun title(): String {
        if (navBackStackEntry?.destination?.hasRoute(AppDestination.Web::class) == true) {
            return navBackStackEntry?.toRoute<AppDestination.Web>()?.title ?: ""
        }

        TabBarItem.entries.forEach { tabItem ->
            navBackStackEntry?.destination?.hierarchy?.forEach {
                if (it.hasRoute(tabItem.route::class)) {
                    return tabItem.title
                }
            }
        }
        return ""
    }

    fun showBackButton(): Boolean {
        return navBackStackEntry?.destination?.hasRoute(AppDestination.Web::class) == true
                && navController.previousBackStackEntry != null
    }

    TopAppBar(
        title = {
            Text(title(),
                overflow = TextOverflow.Ellipsis
            )
        },
        colors =  TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (showBackButton()) {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (navBackStackEntry?.destination?.hasRoute(AppDestination.Web::class) == true) {
                WebActions(webState)
            } else if (navBackStackEntry?.destination?.hasRoute(AppDestination.Qibla::class) == true) {
                LocationCard(null)
            }
        }
    )
}