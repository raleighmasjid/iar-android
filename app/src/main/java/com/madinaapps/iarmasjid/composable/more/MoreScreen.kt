package com.madinaapps.iarmasjid.composable.more

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.BuildConfig
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.viewModel.SettingsViewModel

@Composable
fun MoreScreen(viewModel: SettingsViewModel = hiltViewModel(), paddingValues: PaddingValues) {
    val uriHandler = LocalUriHandler.current
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val versionNumber = BuildConfig.VERSION_NAME
    val buildNumber = BuildConfig.VERSION_CODE

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        MoreRow(
            content = {
                NotificationSoundRow(viewModel, expanded) { expanded = false }
            },
            icon = painterResource(R.drawable.ic_adhan_sound)
        ) {
            expanded = !expanded
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MoreRow(
                content = {
                    Text("Notifications", fontSize = 16.sp)
                },
                icon = painterResource(R.drawable.ic_settings_notifications)
            ) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                context.startActivity(intent, null)
            }
        }

        MoreRow(
            content = {
                Text("View Full Website", fontSize = 16.sp)
            },
            icon = painterResource(R.drawable.ic_full_website),
        ) {
            uriHandler.openUri("https://raleighmasjid.org/")
        }

        Text("App version $versionNumber ($buildNumber)",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text("Open Daily From Fajr to Isha",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DirectionsButton(
                    title = "Atwater St",
                    address = "808 Atwater St, Raleigh, NC 27607",
                    modifier = Modifier.weight(1f)
                )
                DirectionsButton(
                    title = "Page Rd",
                    address = "3104 Page Rd, Morrisville, NC 27560",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }



}

@Preview(showBackground = true)
@Composable
fun MoreScreenPreview() {
    MoreScreen(paddingValues = PaddingValues())
}