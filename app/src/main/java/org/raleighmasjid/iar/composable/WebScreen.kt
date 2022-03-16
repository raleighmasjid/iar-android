package org.raleighmasjid.iar.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.raleighmasjid.iar.LocalNavController
import org.raleighmasjid.iar.R

@Composable
fun WebScreen(url: String) {
    val state by remember { mutableStateOf(WebViewState(WebContent.Url(url))) }
    val navController = LocalNavController.current
    var didStart by remember { mutableStateOf(false) }

    if (state.isLoading) {
        didStart = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = url,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    if (state.isLoading || !didStart) {
                        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(15.dp),
                                color = Color.White
                            )
                        }
                    }
                },
                elevation = 5.dp,
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.navigateUp() }) {
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
    ) {
        WebView(state,
            onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
        })
    }
}