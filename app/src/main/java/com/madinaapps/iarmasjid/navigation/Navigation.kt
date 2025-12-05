package com.madinaapps.iarmasjid.navigation

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.madinaapps.iarmasjid.composable.donate.DonateScreen
import com.madinaapps.iarmasjid.composable.more.MoreScreen
import com.madinaapps.iarmasjid.composable.news.NewsScreen
import com.madinaapps.iarmasjid.composable.prayer.PrayerScreen
import com.madinaapps.iarmasjid.composable.qibla.QiblaScreen
import com.madinaapps.iarmasjid.composable.web.WebContent
import com.madinaapps.iarmasjid.composable.web.WebScreen
import com.madinaapps.iarmasjid.composable.web.WebViewState
import com.madinaapps.iarmasjid.viewModel.NewsViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerScreenViewModel
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    prayerScreenViewModel: PrayerScreenViewModel,
    newsViewModel: NewsViewModel,
    qiblaViewModel: QiblaViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val webState by remember { mutableStateOf(WebViewState(WebContent.Url(""))) }

    val view = LocalView.current
    val density = LocalDensity.current
    val darkMode = isSystemInDarkTheme()

    val hideTopBar = TabBarItem.entries.filter { it.hideTopBar }.any {
        navBackStackEntry?.destination?.hasRoute(it.route::class) == true
    }

    LaunchedEffect(navBackStackEntry) {
        val window = (view.context as Activity).window
        val insets = WindowCompat.getInsetsController(window, view)
        insets.isAppearanceLightStatusBars = (navBackStackEntry?.destination?.hasRoute(AppDestination.Prayer::class) != true && !darkMode)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    navController.addOnDestinationChangedListener { _, _, _ ->
        scrollBehavior.state.contentOffset = 0f
        scrollBehavior.state.heightOffset = 0f
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!hideTopBar) {
                TopNavigationBar(
                    navController,
                    webState,
                    qiblaViewModel,
                    scrollBehavior
                )
            }
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                BottomNavigationBar(navController, newsViewModel)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppDestination.Prayer,
            enterTransition = { NavTransition.defaultEnterTransition },
            exitTransition = { NavTransition.defaultExitTransition }
        ) {
            composable<AppDestination.Prayer> {
                PrayerScreen(prayerScreenViewModel, innerPadding)
            }

            composable<AppDestination.Qibla> {
                QiblaScreen(innerPadding, qiblaViewModel)
            }

            navigation<AppDestination.NewsTab>(AppDestination.News) {
                composable<AppDestination.News>(
                    popEnterTransition = { NavTransition.popEnterTransition() },
                    exitTransition = {
                        if (targetState.destination.hasRoute(AppDestination.Web::class)) {
                            NavTransition.pushExitTransition(density)
                        } else {
                            NavTransition.defaultExitTransition
                        }
                    }
                ) {
                    NewsScreen(newsViewModel, innerPadding) {
                        navController.navigate(it)
                    }
                }

                composable<AppDestination.Web>(
                    enterTransition = { NavTransition.pushEnterTransition() },
                    popExitTransition = {
                        if (targetState.destination.hasRoute(AppDestination.News::class)) {
                            NavTransition.popExitTransition(density)
                        } else {
                            NavTransition.defaultExitTransition
                        }
                    }
                ) { backStackEntry ->
                    webState.content = WebContent.Url(backStackEntry.toRoute<AppDestination.Web>().url)
                    WebScreen(webState, innerPadding)
                }
            }

            composable<AppDestination.Donate> {
                DonateScreen(innerPadding)
            }

            composable<AppDestination.More> {
                MoreScreen(paddingValues = innerPadding)
            }
        }
    }
}
