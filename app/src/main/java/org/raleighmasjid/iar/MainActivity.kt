package org.raleighmasjid.iar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationController.createNotificationChannel(applicationContext)

        setContent {
            IARTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }
}


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Prayer.route) {
        composable(NavigationItem.Prayer.route) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.Prayer.title) })
            }) {
                PrayerScreen()
            }
        }
        composable(NavigationItem.News.route) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = NavigationItem.News.title) })
            }) {
                NewsScreen()
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
