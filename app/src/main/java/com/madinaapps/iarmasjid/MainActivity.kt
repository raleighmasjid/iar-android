package com.madinaapps.iarmasjid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madinaapps.iarmasjid.composable.BottomNavigationBar
import com.madinaapps.iarmasjid.composable.WebScreen
import com.madinaapps.iarmasjid.composable.donate.DonateScreen
import com.madinaapps.iarmasjid.composable.more.MoreScreen
import com.madinaapps.iarmasjid.composable.news.NewsScreen
import com.madinaapps.iarmasjid.composable.prayer.PrayerScreen
import com.madinaapps.iarmasjid.ui.theme.IARTheme
import com.madinaapps.iarmasjid.utils.DayChangedBroadcastReceiver
import com.madinaapps.iarmasjid.utils.NotificationController
import com.madinaapps.iarmasjid.utils.RefreshNotificationsWorker
import com.madinaapps.iarmasjid.utils.Utils
import com.madinaapps.iarmasjid.viewModel.NewsViewModel
import com.madinaapps.iarmasjid.viewModel.PrayerTimesViewModel
import dagger.hilt.android.AndroidEntryPoint

val LocalNavController = compositionLocalOf<NavHostController> { error("missing") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val prayerTimesViewModel: PrayerTimesViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.setupNotificationChannels(applicationContext)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.Transparent.hashCode()))

        setContent {
            IARTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(bottomBar = {
                        BottomNavigationBar(navController, newsViewModel.showBadge,  modifier = Modifier.navigationBarsPadding())
                    }) { innerPadding ->
                        Box(modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)) {
                            Navigation(navController, prayerTimesViewModel, newsViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RefreshNotificationsWorker.schedulePeriodic(applicationContext)
        prayerTimesViewModel.loadData()
        prayerTimesViewModel.didResume = true

        newsViewModel.loadData(forceRefresh = false)

        ContextCompat.registerReceiver(
            this,
            dayChangedBroadcastReceiver,
            DayChangedBroadcastReceiver.getIntentFilter(),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(dayChangedBroadcastReceiver)
    }

    private val dayChangedBroadcastReceiver = object : DayChangedBroadcastReceiver() {

        override fun onDayChanged() {
            prayerTimesViewModel.loadData()
            newsViewModel.loadData(forceRefresh = false)
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    prayerTimesViewModel: PrayerTimesViewModel,
    newsViewModel: NewsViewModel
) {
    NavHost(navController, startDestination = NavigationItem.Prayer.route) {
        composable(
            NavigationItem.Prayer.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            Scaffold(topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    windowInsets = WindowInsets.statusBars,
                    title = { Text(text = NavigationItem.Prayer.title) }
                )
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    PrayerScreen(prayerTimesViewModel)
                }
            }
        }
        composable(
            NavigationItem.News.route,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationItem.BASE_WEB_ROUTE -> null
                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationItem.BASE_WEB_ROUTE -> null
                    else -> ExitTransition.None
                }
            }
        ) {
            Scaffold(topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    windowInsets = WindowInsets.statusBars,
                    title = { Text(text = NavigationItem.News.title) }
                )
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    NewsScreen(newsViewModel)
                }
            }
        }
        composable(
            NavigationItem.Donate.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            Scaffold(topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    windowInsets = WindowInsets.statusBars,
                    title = { Text(text = NavigationItem.Donate.title) }
                )
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    DonateScreen()
                }
            }
        }
        composable(
            NavigationItem.More.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            Scaffold(topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    windowInsets = WindowInsets.statusBars,
                    title = { Text(text = NavigationItem.More.title) }
                )
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    MoreScreen()
                }
            }
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
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
