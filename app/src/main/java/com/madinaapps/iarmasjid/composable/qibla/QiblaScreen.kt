package com.madinaapps.iarmasjid.composable.qibla

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.madinaapps.iarmasjid.model.QiblaMode
import com.madinaapps.iarmasjid.viewModel.QiblaViewModel

@Composable
fun tabTextColor(mode: QiblaMode, selectedMode: QiblaMode): Color {
    return if (mode == selectedMode) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.onSecondary
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QiblaScreen(
    paddingValues: PaddingValues,
    viewModel: QiblaViewModel
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        SecondaryTabRow(
            viewModel.mode.index,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {}
        ) {
            QiblaMode.entries.forEach {
                Tab(
                    selected = viewModel.mode == it,
                    onClick = { viewModel.mode = it }
                ) {
                    Text(it.title,
                        fontWeight = FontWeight.Medium,
                        color = tabTextColor(it, viewModel.mode),
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                }
            }
        }
        when(viewModel.mode) {
            QiblaMode.MAP -> QiblaMap(viewModel)
            QiblaMode.COMPASS -> QiblaCompass(viewModel)
        }
    }
}
