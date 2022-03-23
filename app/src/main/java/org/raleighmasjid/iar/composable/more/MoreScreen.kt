package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.raleighmasjid.iar.BuildConfig
import org.raleighmasjid.iar.composable.more.MoreRow
import org.raleighmasjid.iar.model.NotificationType
import org.raleighmasjid.iar.viewModel.SettingsViewModel

@Composable
fun MoreScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uriHandler = LocalUriHandler.current
    var expanded by remember { mutableStateOf(false) }
    val notificationType = viewModel.dataStoreManager.getNotificationType().collectAsState(initial = viewModel.currentNotificationType())
    val scope = rememberCoroutineScope()
    val versionNumber = BuildConfig.VERSION_NAME
    val buildNumber = BuildConfig.VERSION_CODE

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Box {
            MoreRow(onClick = {
                expanded = true
            }) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Prayer Alert", modifier = Modifier.weight(1f))
                    Text(notificationType.value.title(), modifier = Modifier.padding(end = 20.dp))
                }

            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }) {
                NotificationType.options().forEach { notifType ->
                    DropdownMenuItem(onClick = {
                        scope.launch {
                            viewModel.dataStoreManager.setNotificationType(notifType)
                        }

                        expanded = false
                    }) {
                        Text(notifType.title())
                    }
                    if (notifType != NotificationType.options().lastOrNull()) {
                        Divider()
                    }
                }
            }
        }

        MoreRow(onClick = {
            uriHandler.openUri("https://raleighmasjid.org/appfeedback")
        }) {
            Text("App Feedback")
        }

        MoreRow(onClick = {
            uriHandler.openUri("https://raleighmasjid.org/")
        }) {
            Text("Visit Full Website")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
            Text(text = "The Islamic Assocation of Raleigh", fontSize = 12.sp, color = Color.Gray)
            Text(text = "Version $versionNumber ($buildNumber)", fontSize = 12.sp, color = Color.Gray)
        }

        
    }
}

@Preview(showBackground = true)
@Composable
fun MoreScreenPreview() {
    MoreScreen()
}