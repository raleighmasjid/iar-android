package com.madinaapps.iarmasjid.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import com.madinaapps.iarmasjid.LocalNavController
import com.madinaapps.iarmasjid.R

@Composable
fun WebScreen(url: String) {
    val state by remember { mutableStateOf(WebViewState(WebContent.Url(url))) }
    val navController = LocalNavController.current
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
                data = Uri.parse(currentUrl)
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
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
                                DropdownMenuItem(onClick = {
                                    shareUrl()
                                    expanded = false
                                }) {
                                    Text("Share")
                                }
                                Divider()
                                DropdownMenuItem(onClick = {
                                    openUrl()
                                    expanded = false
                                }) {
                                    Text("Open in Browser")
                                }
                            }
                        }

                    }

                },
                elevation = 5.dp,
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(painterResource(id = R.drawable.ic_back_chevron),
                                contentDescription = "Back"
                            )
                        }
                    }
                } else {
                    null
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            WebView(state,
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                }
            )
        }
    }
}