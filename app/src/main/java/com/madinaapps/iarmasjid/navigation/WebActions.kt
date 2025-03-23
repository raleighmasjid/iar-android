package com.madinaapps.iarmasjid.navigation

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.madinaapps.iarmasjid.composable.web.WebViewState

@Composable
fun WebActions(state: WebViewState) {
    val context = LocalContext.current
    var expandedDropdown by remember { mutableStateOf(false) }

    fun shareUrl() {
        val currentUrl = state.content.getCurrentUrl()
        if (currentUrl != null) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, currentUrl)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    fun openUrl() {
        val currentUrl = state.content.getCurrentUrl()
        if (currentUrl != null) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = currentUrl.toUri()
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    IconButton(onClick = {
        expandedDropdown = true
    }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "Actions")
    }
    DropdownMenu(
        expanded = expandedDropdown,
        onDismissRequest = {
            expandedDropdown = false
        }
    ) {
        DropdownMenuItem(
            text = { Text("Share") },
            onClick = {
                shareUrl()
            }
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSecondary
        )
        DropdownMenuItem(
            text = { Text("Open in Browser") },
            onClick = {
                openUrl()
            }
        )
    }
}