package com.madinaapps.iarmasjid.navigation

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

@Composable
fun WebActions() {
    var expandedDropdown by remember { mutableStateOf(false) }

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
            }
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSecondary
        )
        DropdownMenuItem(
            text = { Text("Open in Browser") },
            onClick = {
            }
        )
    }
}