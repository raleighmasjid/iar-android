package org.raleighmasjid.iar.composable

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.viewinterop.AndroidView

/**
 * A wrapper around the Android View WebView to provide a basic WebView composable.
 *
 * If you require more customisation you are most likely better rolling your own and using this
 * wrapper as an example.
 *
 * @param state The webview state holder where the Uri to load is defined.
 * @param captureBackPresses Set to true to have this Composable capture back presses and navigate
 * the WebView back.
 * @param onCreated Called when the WebView is first created, this can be used to set additional
 * settings on the WebView.
 * @param onPageStarted Called when the WebView starts loading a page. Forwarded event from the
 * WebViewClient.
 * @param onPageFinished Called when the WebView finishes loading a page. Forwarded event from the
 * WebViewClient
 * @param onError Called when the WebView encounters an error. Forwarded event from the
 * WebViewClient
 * @sample com.google.accompanist.sample.webview.BasicWebViewSample
 */
@Composable
fun WebView(
    state: WebViewState,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    onCreated: (WebView) -> Unit = {},
    onError: (request: WebResourceRequest?, error: WebResourceError?) -> Unit = { _, _ -> }
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack: Boolean by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    BackHandler(captureBackPresses && canGoBack) {
        webView?.goBack()
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                onCreated(this)

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        state.isLoading = true
                        state.errorsForCurrentRequest.clear()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        state.isLoading = false
                        canGoBack = view?.canGoBack() ?: false
                    }

                    override fun doUpdateVisitedHistory(
                        view: WebView?,
                        url: String?,
                        isReload: Boolean
                    ) {
                        super.doUpdateVisitedHistory(view, url, isReload)
                        // WebView will often update the current url itself.
                        // This happens in situations like redirects and navigating through
                        // history. We capture this change and update our state holder url.
                        // On older APIs (28 and lower), this method is called when loading
                        // html data. We don't want to update the state in this case as that will
                        // overwrite the html being loaded.
                        if (url != null &&
                            !url.startsWith("data:text/html") &&
                            state.content.getCurrentUrl() != url
                        ) {
                            state.content = WebContent.Url(url)
                        }
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)

                        if (error != null) {
                            state.errorsForCurrentRequest.add(WebViewError(request, error))
                        }

                        onError(request, error)
                    }

                    fun utilShouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                        if (URLUtil.isNetworkUrl(url)) {
                            val content = WebContent.Url(url)
                            state.content = content
                        } else {
                            try {
                                uriHandler.openUri(url)
                            } catch (e: Exception) {
                                Log.d("INFO", "error opening uri $url")
                                if (url.startsWith("intent://")) {
                                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                                    val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                                    if (fallbackUrl != null) {
                                        webView?.loadUrl(fallbackUrl)
                                    }
                                }
                            }
                        }
                        return true
                    }

                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        if (url != null) {
                            return utilShouldOverrideUrlLoading(view, url)
                        }
                        return false
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url?.toString() ?: return false
                        return utilShouldOverrideUrlLoading(view, url)
                    }
                }
            }.also { webView = it }
        },
        modifier = modifier
    ) { view ->
        when (val content = state.content) {
            is WebContent.Url -> {
                val url = content.url

                if (url.isNotEmpty() && url != view.url) {
                    view.loadUrl(url)
                }
            }
            is WebContent.Data -> {
                view.loadDataWithBaseURL(content.baseUrl, content.data, null, "utf-8", null)
            }
        }

        canGoBack = view.canGoBack()
    }
}

sealed class WebContent {
    data class Url(val url: String) : WebContent()
    data class Data(val data: String, val baseUrl: String? = null) : WebContent()

    fun getCurrentUrl(): String? {
        return when (this) {
            is Url -> url
            is Data -> baseUrl
        }
    }
}

/**
 * A state holder to hold the state for the WebView. In most cases this will be remembered
 * using the rememberWebViewState(uri) function.
 */
@Stable
class WebViewState(webContent: WebContent) {
    /**
     *  The content being loaded by the WebView
     */
    var content by mutableStateOf<WebContent>(webContent)

    /**
     * Whether the WebView is currently loading data in its main frame
     */
    var isLoading: Boolean by mutableStateOf(false)
        internal set

    /**
     * A list for errors captured in the last load. Reset when a new page is loaded.
     * Errors could be from any resource (iframe, image, etc.), not just for the main page.
     * For more fine grained control use the OnError callback of the WebView.
     */
    val errorsForCurrentRequest = mutableStateListOf<WebViewError>()
}

/**
 * A wrapper class to hold errors from the WebView.
 */
@Immutable
data class WebViewError(
    /**
     * The request the error came from.
     */
    val request: WebResourceRequest?,
    /**
     * The error that was reported.
     */
    val error: WebResourceError
)

/**
 * Creates a WebView state that is remembered across Compositions.
 *
 * @param url The url to load in the WebView
 */
@Composable
fun rememberWebViewState(url: String) =
    remember(url) { WebViewState(WebContent.Url(url)) }

/**
 * Creates a WebView state that is remembered across Compositions.
 *
 * @param data The uri to load in the WebView
 */
@Composable
fun rememberWebViewStateWithHTMLData(data: String, baseUrl: String? = null) =
    remember(data, baseUrl) { WebViewState(WebContent.Data(data, baseUrl)) }
