package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.raleighmasjid.iar.composable.DonateScreen
import org.raleighmasjid.iar.composable.NewsScreen
import org.raleighmasjid.iar.composable.PrayerScreen
import org.raleighmasjid.iar.ui.theme.IARTheme
import org.raleighmasjid.iar.utils.NotificationController
import org.raleighmasjid.iar.viewModel.PrayerTimesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: PrayerTimesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.createNotificationChannel(applicationContext)

        setContent {
            IARTheme {
                val navController = rememberNavController()

                Scaffold(bottomBar = { BottomNavigationBar(navController) }) {
                    Navigation(navController = navController, viewModel = viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchLatest()
    }
}


@Composable
fun Navigation(navController: NavHostController, viewModel: PrayerTimesViewModel) {
    NavHost(navController, startDestination = NavigationItem.Prayer.route) {
        composable(NavigationItem.Prayer.route) {
            PrayerScreen(viewModel)
        }
        composable(NavigationItem.Donate.route) {
            DonateScreen()
        }
        composable(NavigationItem.News.route) {
            NewsScreen()
        }
    }
}
