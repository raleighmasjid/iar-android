package com.madinaapps.iarmasjid.composable.prayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesLoadError(dismissAction: () -> Unit, retryAction: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = {
            dismissAction()
        }
    ) {
        Surface(modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults. TonalElevation
        ) {
            Column(modifier = Modifier. padding(16.dp)) {
                Text("Error",
                    modifier = Modifier
                        .padding(bottom = 16.dp) // space between title and content
                        .semantics { heading() },
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("Unable to load prayer times",
                    modifier = Modifier
                        .padding(bottom = 24.dp))

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = { dismissAction() }
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            retryAction()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}