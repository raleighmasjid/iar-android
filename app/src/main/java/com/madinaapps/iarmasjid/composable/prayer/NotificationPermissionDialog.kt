package com.madinaapps.iarmasjid.composable.prayer

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun NotificationPermissionDialog(dismiss: () -> Unit, cancel: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            cancel()
        },
        title = { Text(text = "Notification Permissions") },
        text = { Text(text = "Please enable notification permissions to turn on prayer alerts.") },
        confirmButton = {
            Button(
                onClick = {
                    dismiss()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                        context.startActivity(intent, null)
                    }
                }
            ) {
                Text(
                    text = "Settings",
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    cancel()
                }
            ) {
                Text(
                    text = "Cancel",
                )
            }
        }
    )
}