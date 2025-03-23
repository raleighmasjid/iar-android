package com.madinaapps.iarmasjid.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madinaapps.iarmasjid.composable.WebScreen
import com.madinaapps.iarmasjid.composable.donate.DonateScreen
import com.madinaapps.iarmasjid.composable.more.MoreScreen
import com.madinaapps.iarmasjid.composable.news.NewsScreen
import com.madinaapps.iarmasjid.composable.prayer.PrayerScreen
import com.madinaapps.iarmasjid.utils.Utils
import com.madinaapps.iarmasjid.viewModel.NewsViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
    prayerTimesViewModel: PrayerTimesViewModel,
    newsViewModel: NewsViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val view = LocalView.current
    val darkMode = isSystemInDarkTheme()
    val defaultEnterTransition = fadeIn(animationSpec = tween(220))
    val defaultExitTransition = fadeOut(animationSpec = tween(220))

    LaunchedEffect(navBackStackEntry) {
        val window = (view.context as Activity).window
        val insets = WindowCompat.getInsetsController(window, view)
        insets.isAppearanceLightStatusBars = (navBackStackEntry?.destination?.route != NavigationItem.Prayer.route && !darkMode)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (navBackStackEntry?.destination?.route != NavigationItem.Prayer.route) {
                TopAppBar(
                    title = {
                        Text(NavigationItem.routeTitle(navBackStackEntry))
                    },
                    navigationIcon = {
                        if (navBackStackEntry?.destination?.route == NavigationItem.BASE_WEB_ROUTE && navController.previousBackStackEntry != null) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = NavigationItem.Prayer.route,
        ) {
            composable(
                NavigationItem.Prayer.route,
                enterTransition = { defaultEnterTransition },
                exitTransition = { defaultExitTransition }
            ) {
                PrayerScreen(prayerTimesViewModel, innerPadding)
            }

            composable(
                NavigationItem.News.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        NavigationItem.BASE_WEB_ROUTE -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(220)
                        )
                        else -> defaultEnterTransition
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        NavigationItem.BASE_WEB_ROUTE -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(220)
                        )
                        else -> defaultExitTransition
                    }
                }
            ) {
                NewsScreen(newsViewModel, innerPadding)
            }

            composable(
                NavigationItem.Donate.route,
                enterTransition = { defaultEnterTransition },
                exitTransition = { defaultExitTransition }
            ) {
                DonateScreen()
            }

            composable(
                NavigationItem.More.route,
                enterTransition = { defaultEnterTransition },
                exitTransition = { defaultExitTransition }
            ) {
                MoreScreen()
            }

            composable(
                NavigationItem.BASE_WEB_ROUTE,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(220)
                    )
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        NavigationItem.News.route -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(220)
                        )
                        else -> defaultExitTransition
                    }
                }
            ) { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url") ?: ""
                val decodedUrl = Utils.decodeURL(url)
                WebScreen(decodedUrl)
            }
        }
    }
}
