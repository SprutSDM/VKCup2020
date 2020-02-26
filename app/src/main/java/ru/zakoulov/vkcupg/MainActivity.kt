package ru.zakoulov.vkcupg

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.zakoulov.vkcupg.ui.marketslist.MarketsListFragment
import ru.zakoulov.vkcupg.ui.welcome.WelcomeFragment

class MainActivity : AppCompatActivity() {

    private var shouldNavigateAfterOnActivityResult: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (VK.isLoggedIn()) {
                (application as App).marketsRepository.fetchCities()
                navigateToMarkets()
            } else {
                navigateToWelcome()
            }
        }

        (application as App).tokenExpired.observe(this) {
            if (it == true) {
                navigateToWelcome()
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        if (shouldNavigateAfterOnActivityResult) {
            shouldNavigateAfterOnActivityResult = false
            navigateToMarkets()
        }
    }

    fun login() {
        VK.login(this, listOf(VKScope.MARKET, VKScope.GROUPS))
    }

    fun navigateToWelcome() = navigateTo(WelcomeFragment.instance, WelcomeFragment.TAG)

    fun navigateToMarkets() {
        navigateTo(MarketsListFragment.INSTANCE, MarketsListFragment.TAG)
    }

    private fun navigateTo(fragment: Fragment, tagFragment: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tagFragment)
            .commit()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                (application as App).tokenExpired.value = false
                shouldNavigateAfterOnActivityResult = true
                (application as App).marketsRepository.fetchCities()
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
