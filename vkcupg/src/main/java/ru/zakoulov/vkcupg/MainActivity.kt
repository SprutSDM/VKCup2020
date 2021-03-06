package ru.zakoulov.vkcupg

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.zakoulov.vkcupg.ui.marketslist.MarketsListFragment
import ru.zakoulov.vkcupg.ui.productinfo.ProductInfoFragment
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
            putInt(ProductsListFragment.KEY_MARKET_ID, marketId)
        }
        navigateTo(fragment, ProductsListFragment.TAG, addToBackStack = true)
    }

    fun navigateToProductInfo(marketId: Int, productId: Int, sharedView: View) {
        val transition = TransitionInflater.from(this)
            .inflateTransition(R.transition.image_shared_element_transition)
        val fragment = ProductInfoFragment()
        fragment.sharedElementEnterTransition = transition
        fragment.sharedElementReturnTransition = transition
        fragment.enterTransition = Fade().removeTarget(R.id.product_photo_info).removeTarget(sharedView)
        fragment.exitTransition = Fade().removeTarget(R.id.product_photo_info).removeTarget(sharedView)
        fragment.reenterTransition = Fade().removeTarget(R.id.product_photo_info).removeTarget(sharedView)
        fragment.returnTransition = Fade().removeTarget(R.id.product_photo_info).removeTarget(sharedView)
        fragment.arguments = Bundle().apply {
            putInt(ProductInfoFragment.KEY_MARKET_ID, marketId)
            putInt(ProductInfoFragment.KEY_PRODUCT_ID, productId)
            putString(ProductInfoFragment.KEY_TRANSITION_NAME, sharedView.transitionName)
        }
        navigateTo(fragment, ProductInfoFragment.TAG, addToBackStack = true, sharedElements = listOf(sharedView))
    }

    fun navigateBack() {
        supportFragmentManager.popBackStack()
    }

    private fun navigateTo(fragment: Fragment,
                           tagFragment: String,
                           addToBackStack: Boolean = false,
                           sharedElements: List<View> = emptyList()) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tagFragment)
        sharedElements.forEach { sharedElement ->
            transaction.addSharedElement(sharedElement, sharedElement.transitionName)
        }
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

    fun getScreenWidth(): Int {
        val size = Point()
        windowManager?.defaultDisplay?.getSize(size)
        return size.x
    }
}
