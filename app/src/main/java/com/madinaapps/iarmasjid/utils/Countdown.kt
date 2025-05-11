package com.madinaapps.iarmasjid.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun Countdown(targetTime: Long, content: @Composable (remainingTime: Long) -> Unit) {
    val context = LocalContext.current
    var remainingTime by remember {
        mutableLongStateOf(targetTime - System.currentTimeMillis())
    }
    var isRunning by remember { mutableStateOf(false) }

    content.invoke(remainingTime)

    DisposableEffect(context) {
        isRunning = true
        onDispose {
            isRunning = false
        }
    }

    LifecycleResumeEffect(Unit) {
        isRunning = true
        onPauseOrDispose {
            isRunning = false
        }
    }

    LaunchedEffect(isRunning, targetTime) {
        while (isRunning) {
            remainingTime = targetTime - Date().time
            delay(1000 - Date().time % 1000)
        }
    }
}