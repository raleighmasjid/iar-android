package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.raleighmasjid.iar.composable.DonateScreen
import org.raleighmasjid.iar.composable.MoreScreen
import org.raleighmasjid.iar.composable.NewsScreen
import org.raleighmasjid.iar.composable.PrayerScreen
import org.raleighmasjid.iar.ui.theme.IARTheme
import org.raleighmasjid.iar.utils.NotificationController
import org.raleighmasjid.iar.viewModel.NewsViewModel
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val prayerTimesViewModel: PrayerTimesViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.createNotificationChannel(applicationContext)

        setContent {
            IARTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Navigation(navController, prayerTimesViewModel, newsViewModel)
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
    }
}


@Composable
fun Navigation(
    navController: NavHostController,
    prayerTimesViewModel: PrayerTimesViewModel,
    newsViewModel: NewsViewModel
) {
    NavHost(navController, startDestination = NavigationItem.Prayer.route) {
        composable(NavigationItem.Prayer.route) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.Prayer.title) })
            }) {
                PrayerScreen(prayerTimesViewModel)
            }
        }
        composable(NavigationItem.News.route) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.News.title) })
            }) {
                NewsScreen(newsViewModel)
            }
        }
        composable(NavigationItem.Donate.route) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.Donate.title) })
            }) {
                DonateScreen()
            }
        }
        composable(NavigationItem.More.route) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.More.title) })
            }) {
                MoreScreen()
            }
        }
    }
}
