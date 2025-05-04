package com.madinaapps.iarmasjid.composable.more

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
                    Text("Notifications", fontSize = 17.sp)
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
                Text("View Full Website", fontSize = 17.sp)
            },
            icon = painterResource(R.drawable.ic_full_website)
        ) {
            uriHandler.openUri("https://raleighmasjid.org/")
        }
    }

//            ExposedDropdownMenuBox(
//                modifier = Modifier.border(
//                    width = 1.dp,
//                    color = Color.Blue,
//                ),
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }) {
//                TextField(
//                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).width(
//                        IntrinsicSize.Min),
//                    value = notificationType.value.title(),
//                    onValueChange = {},
//                    readOnly = true,
//                    singleLine = true,
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                    },
//                    colors = ExposedDropdownMenuDefaults.textFieldColors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent
//                    )
//                )

//            }
//        }

//        Box {
//            MoreRow(onClick = {
//                expanded = true
//            }) {
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Text("Prayer Alert", modifier = Modifier.weight(1f))
//                    Text(notificationType.value.title(), modifier = Modifier.padding(end = 20.dp))
//                }
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = {
//                    expanded = false
//                }) {
//                NotificationType.options().forEach { notifType ->
//                    DropdownMenuItem(onClick = {
//                        scope.launch {
//                            viewModel.dataStoreManager.setNotificationType(notifType)
//                        }
//
//                        expanded = false
//                    }) {
//                        Text(notifType.title())
//                    }
//                    if (notifType != NotificationType.options().lastOrNull()) {
//                        HorizontalDivider()
//                    }
//                }
//            }
//        }

//        MoreRow(onClick = {
//            uriHandler.openUri("https://raleighmasjid.org/appfeedback")
//        }) {
//            Text("App Feedback")
//        }
//
//        MoreRow(onClick = {
//            uriHandler.openUri("https://raleighmasjid.org/")
//        }) {
//            Text("Visit Full Website")
//        }
//
//        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
//            Text(text = "The Islamic Association of Raleigh", fontSize = 12.sp, color = Color.Gray)
//            Text(text = "Version $versionNumber ($buildNumber)", fontSize = 12.sp, color = Color.Gray)
//        }

}

@Preview(showBackground = true)
@Composable
fun MoreScreenPreview() {
    MoreScreen(paddingValues = PaddingValues())
}