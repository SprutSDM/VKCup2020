package ru.zakoulov.vkcupa.ui.docsviewer

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class FileWebViewClient(private val callbacks: PageLoadingCallbacks) : WebViewClient() {

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        callbacks.pageFinished()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        return false
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return false
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        callbacks.receiveError()
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        callbacks.receiveError()
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        callbacks.receiveError()
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        callbacks.receiveError()
    }
}
