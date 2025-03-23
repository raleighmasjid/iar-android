package com.madinaapps.iarmasjid.composable.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madinaapps.iarmasjid.BuildConfig
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.viewModel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun MoreScreen(viewModel: SettingsViewModel = hiltViewModel(), paddingValues: PaddingValues) {
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
            .padding(paddingValues)
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
                        HorizontalDivider()
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
            Text(text = "The Islamic Association of Raleigh", fontSize = 12.sp, color = Color.Gray)
            Text(text = "Version $versionNumber ($buildNumber)", fontSize = 12.sp, color = Color.Gray)
        }

        
    }
}

@Preview(showBackground = true)
@Composable
fun MoreScreenPreview() {
    MoreScreen(paddingValues = PaddingValues())
}