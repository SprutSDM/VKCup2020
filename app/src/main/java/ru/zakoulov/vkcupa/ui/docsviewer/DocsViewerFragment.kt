package ru.zakoulov.vkcupa.ui.docsviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import ru.zakoulov.vkcupa.R

class DocsViewerFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorContainer: View
    private lateinit var buttonRetry: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_docs_viewer, container, false)
        with (root) {
            webView = findViewById(R.id.web_view)
            progressBar = findViewById(R.id.progress_bar)
            errorContainer = findViewById(R.id.error_container)
            buttonRetry = findViewById(R.id.but_retry)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString(KEY_URL_FOR_OPEN)!!
        val docTitle = arguments?.getString(KEY_DOC_TITLE)!!
        webView.webViewClient = FileWebViewClient(object : PageLoadingCallbacks {
            override fun pageFinished() {
                showLoaded()
            }

            override fun receiveError() {
                showError()
            }
        })
        webView.settings.apply {
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        buttonRetry.setOnClickListener {
            reloadWebView()
        }
        loadUrl(url)
        activity?.title = docTitle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.stopLoading()
    }

    fun showLoaded() {
        errorContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    fun showLoading() {
        errorContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError() {
        errorContainer.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun loadUrl(url: String) {
        showLoading()
        webView.loadUrl(url)
    }

    fun reloadWebView() {
        showLoading()
        webView.reload()
    }

    companion object {
        const val TAG = "DocsViewerFragment"
        const val KEY_URL_FOR_OPEN = "key_url_for_open"
        const val KEY_DOC_TITLE = "key_doc_title"
        val ALLOWED_EXTS = listOf("jpg", "jpeg", "png", "gif", "xlsx", "docx", "pptx", "doc")

        fun newInstance() = DocsViewerFragment()
    }
}
