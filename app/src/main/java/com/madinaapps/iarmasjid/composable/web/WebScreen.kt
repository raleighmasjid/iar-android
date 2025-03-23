package com.madinaapps.iarmasjid.composable.web

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebScreen(url: String, paddingValues: PaddingValues) {
    val state by remember { mutableStateOf(WebViewState(WebContent.Url(url))) }
    var didStart by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (state.isLoading) {
        didStart = true
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Box {
            WebView(state,
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                }
            )
            if (state.isLoading || !didStart) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}