package com.madinaapps.iarmasjid.navigation

import android.app.Activity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    prayerTimesViewModel: PrayerTimesViewModel,
    newsViewModel: NewsViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val webState by remember { mutableStateOf(WebViewState(WebContent.Url(""))) }

    val view = LocalView.current
    val darkMode = isSystemInDarkTheme()

    val tweenDuration = 220
    val defaultEnterTransition = fadeIn(animationSpec = tween(tweenDuration))
    val defaultExitTransition = fadeOut(animationSpec = tween(tweenDuration))

    val hideTopBar = TabBarItem.entries.filter { it.hideTopBar }.any {
        navBackStackEntry?.destination?.hasRoute(it.route::class) == true
    }

    LaunchedEffect(navBackStackEntry) {
        val window = (view.context as Activity).window
        val insets = WindowCompat.getInsetsController(window, view)
        insets.isAppearanceLightStatusBars = (navBackStackEntry?.destination?.hasRoute(AppDestination.Prayer::class) != true && !darkMode)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!hideTopBar) {
                TopNavigationBar(navController, webState)
            }
        },
        bottomBar = {
            BottomNavigationBar(navController, newsViewModel)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppDestination.Prayer,
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition }
        ) {
            composable<AppDestination.Prayer> {
                PrayerScreen(prayerTimesViewModel, innerPadding)
            }

            composable<AppDestination.Qibla> {
                QiblaScreen(innerPadding)
            }

            composable<AppDestination.Donate> {
                DonateScreen(innerPadding)
            }

            composable<AppDestination.More> {
                MoreScreen(paddingValues = innerPadding)
            }

            composable<AppDestination.News> {
                NewsScreen(newsViewModel, innerPadding)
            }

            composable<AppDestination.Web> { backStackEntry ->
                webState.content = WebContent.Url(backStackEntry.toRoute<AppDestination.Web>().url)
                WebScreen(webState, innerPadding)
            }
        }
    }
}
