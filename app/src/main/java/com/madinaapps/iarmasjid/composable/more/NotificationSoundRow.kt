package com.madinaapps.iarmasjid.composable.more

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madinaapps.iarmasjid.R
import com.madinaapps.iarmasjid.model.NotificationType
import com.madinaapps.iarmasjid.viewModel.SettingsViewModel

@Composable
fun NotificationSoundRow(viewModel: SettingsViewModel, expanded: Boolean, dismissAction: () -> Unit) {
    val notificationType = viewModel.notificationType.collectAsState()

    @Composable
    fun dropdownArrow(): Painter {
        if (expanded) {
            return painterResource(R.drawable.arrow_drop_up_24)
        }
        return painterResource(R.drawable.arrow_drop_down_24)
    }

    Row {
        Text("Adhan Sound", fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Row {
                Text(notificationType.value.title(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Icon(
                    dropdownArrow(),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 4.dp, end = 6.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            DropdownMenu(
                expanded = expanded,
                offset = DpOffset(0.dp, 12.dp),
                onDismissRequest = {
                    dismissAction()
                }
            ) {
                NotificationType.options().forEach { notifType ->
                    DropdownMenuItem(
                        text = {
                            Text(notifType.title(), fontSize = 16.sp)
                        },
                        leadingIcon = {
                            if (notificationType.value == notifType) {
                                Icon(
                                    painterResource(id = R.drawable.check_24px),
                                    contentDescription = "Selected"
                                )
                            }
                        },
                        onClick = {
                            viewModel.setNotificationType(notifType)
                            dismissAction()
                        }
                    )
                }
            }
        }
    }
}
