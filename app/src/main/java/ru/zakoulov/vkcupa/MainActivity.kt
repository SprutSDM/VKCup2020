package ru.zakoulov.vkcupa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.ui.docsviewer.DocsViewerFragment
import ru.zakoulov.vkcupa.ui.main.MainFragment
import ru.zakoulov.vkcupa.ui.welcome.WelcomeFragment
import android.net.Uri
import java.util.Locale
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (VK.isLoggedIn()) {
                navigateToMain()
            } else {
                navigateToWelcome()

            }
        }

        (application as App).tokenExpired.observe(this) {
            if (it != null && it != 0) {
                navigateToWelcome()
            }
        }
    }

    fun login() {
        VK.login(this, listOf(VKScope.DOCS))
    }

    fun navigateToWelcome() = navigateTo(WelcomeFragment.instance)

    fun navigateToMain() = navigateTo(MainFragment.instance)

    fun openDocsViewer(document: Document) {
        if (document.fileExtension.toLowerCase(Locale.getDefault()) !in DocsViewerFragment.ALLOWED_EXTS) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(document.url))
            startActivity(browserIntent)
            return
        }
        val docsViewFragment = DocsViewerFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(DocsViewerFragment.KEY_URL_FOR_OPEN, document.url)
        bundle.putString(DocsViewerFragment.KEY_DOC_TITLE, document.title)
        docsViewFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, docsViewFragment)
            .addToBackStack(DocsViewerFragment.TAG)
            .commitAllowingStateLoss()
    }

    fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                (application as App).documentRepository.loadDocuments(trackProgress = true)
                navigateToMain()
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
