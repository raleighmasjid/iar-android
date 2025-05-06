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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.BuildConfig
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.viewModel.SettingsViewModel
import java.net.URLEncoder

@Composable
fun MoreScreen(viewModel: SettingsViewModel = hiltViewModel(), paddingValues: PaddingValues) {
    val uriHandler = LocalUriHandler.current
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val versionNumber = BuildConfig.VERSION_NAME
    val buildNumber = BuildConfig.VERSION_CODE
    val address = "808 Atwater St, Raleigh, NC 27607"

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
            showDivider = false
        ) {
            uriHandler.openUri("https://raleighmasjid.org/")
        }

        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Open Daily From Fajr to Isha",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(
                onClick = {
                    val addressParam: String = URLEncoder.encode(address, Charsets.UTF_8.name())
                    uriHandler.openUri("https://www.google.com/maps/dir/?api=1&destination=$addressParam")
                },
                elevation = null,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                modifier = Modifier
                    .padding(top = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_location_marker),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(12.dp, 14.dp)
                    )
                    Text(address,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy((-4).dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 30.dp)
            ) {
                Text("The Islamic Association of Raleigh",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text("App version $versionNumber ($buildNumber)",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSecondary
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