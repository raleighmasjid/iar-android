package com.madinaapps.iarmasjid.composable.prayer

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPermissionDialog(dismiss: () -> Unit, cancel: () -> Unit) {
    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = {
            cancel()
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier. padding(16.dp)) {
                Text("Notification Permissions",
                    modifier = Modifier
                        .padding(bottom = 12.dp) // space between title and content
                        .semantics { heading() },
                    style = MaterialTheme.typography.titleLarge
                )
                Text("Please enable notification permissions to turn on prayer alerts.",
                    modifier = Modifier
                        .padding(bottom = 24.dp))

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = { cancel() }
                    ) {
                        Text("Cancel", fontSize = 16.sp)
                    }
                    TextButton(
                        onClick = {
                            dismiss()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                                context.startActivity(intent, null)
                            }
                        }
                    ) {
                        Text("Settings", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}