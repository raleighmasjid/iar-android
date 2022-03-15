package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import org.raleighmasjid.iar.composable.*
import org.raleighmasjid.iar.ui.theme.IARTheme
import org.raleighmasjid.iar.utils.NotificationController
import org.raleighmasjid.iar.utils.Utils
import org.raleighmasjid.iar.viewModel.NewsViewModel
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

val LocalNavController = compositionLocalOf<NavHostController> { error("missing") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val prayerTimesViewModel: PrayerTimesViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.createNotificationChannel(applicationContext)

        setContent {
            IARTheme {
                val navController = rememberAnimatedNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(bottomBar = {
                        BottomNavigationBar(navController)
                    }) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Navigation(navController, prayerTimesViewModel, newsViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        prayerTimesViewModel.fetchLatest()
        prayerTimesViewModel.didResume = true

        newsViewModel.fetchLatest()

        this?.registerReceiver(
            dayChangedBroadcastReceiver,
            DayChangedBroadcastReceiver.getIntentFilter()
        )
    }

    override fun onPause() {
        super.onPause()
        this?.unregisterReceiver(dayChangedBroadcastReceiver)
    }

    private val dayChangedBroadcastReceiver = object : DayChangedBroadcastReceiver() {

        override fun onDayChanged() {
            prayerTimesViewModel.fetchLatest()
            newsViewModel.fetchLatest()
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun Navigation(
    navController: NavHostController,
    prayerTimesViewModel: PrayerTimesViewModel,
    newsViewModel: NewsViewModel
) {
    AnimatedNavHost(navController, startDestination = NavigationItem.Prayer.route) {
        composable(
            NavigationItem.Prayer.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.Prayer.title) })
            }) {
                PrayerScreen(prayerTimesViewModel)
            }
        }
        composable(
            NavigationItem.News.route,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationItem.baseWebRoute -> null
                    else -> EnterTransition.None
                }
            }
        ) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.News.title) })
            }) {
                NewsScreen(newsViewModel)
            }
        }
        composable(
            NavigationItem.Donate.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.Donate.title) })
            }) {
                DonateScreen()
            }
        }
        composable(
            NavigationItem.More.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.More.title) })
            }) {
                MoreScreen()
            }
        }
        composable(
            NavigationItem.baseWebRoute,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(220)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(220)
                )
            }
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            val decodedUrl = Utils.decodeURL(url)
            WebScreen(decodedUrl)
        }
    }
}
