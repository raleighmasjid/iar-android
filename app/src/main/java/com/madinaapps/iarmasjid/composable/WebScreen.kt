package com.madinaapps.iarmasjid.composable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.madinaapps.iarmasjid.R

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebScreen(url: String) {
    val state by remember { mutableStateOf(WebViewState(WebContent.Url(url))) }
    var didStart by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White,
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(text = state.content.getCurrentUrl() ?: url,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        if (state.isLoading || !didStart) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(15.dp),
                                color = Color.White
                            )
                        }

                        Box {
                            IconButton(onClick = {
                                expanded = true
                            }) {
                                Icon(painterResource(id = R.drawable.ic_more),
                                    contentDescription = "Actions"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }) {
                                DropdownMenuItem(
                                    onClick = {
                                        shareUrl()
                                        expanded = false
                                    },
                                    text = { Text("Share") }
                                    )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    onClick = {
                                    openUrl()
                                    expanded = false
                                    },
                                    text = { Text("Open in Browser") }
                                )
                            }
                        }

                    }

                },
                elevation = 5.dp,
//                navigationIcon = if (navController.previousBackStackEntry != null) {
//                    {
//                        IconButton(onClick = {
//                            navController.navigateUp()
//                        }) {
//                            Icon(painterResource(id = R.drawable.ic_back_chevron),
//                                contentDescription = "Back"
//                            )
//                        }
//                    }
//                } else {
//                    null
//                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            Box {
                WebView(state,
                    onCreated = { webView ->
                        webView.settings.javaScriptEnabled = true
                    },
                    modifier = Modifier.background(Color.Red)
                )
                if (state.isLoading || !didStart) {
                    Column(modifier = Modifier.background(Color.LightGray)) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start)
                                .padding(vertical = 1.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            trackColor = Color.Transparent
                        )
                    }

                }
            }
        }
    }
}