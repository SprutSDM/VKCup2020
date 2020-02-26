package ru.zakoulov.vkcupg

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.zakoulov.vkcupg.ui.marketslist.MarketsListFragment
import ru.zakoulov.vkcupg.ui.productlist.ProductsListFragment
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

    fun navigateToProducts(marketId: Int) {
        val fragment = ProductsListFragment()
        fragment.arguments = Bundle().apply {
            putInt(ProductsListFragment.KEY_MERKET_ID, marketId)
        }
        navigateTo(fragment, ProductsListFragment.TAG, addToBackStack = true)
    }

    fun navigateBack() {
        supportFragmentManager.popBackStack()
    }

    private fun navigateTo(fragment: Fragment, tagFragment: String, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tagFragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
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
