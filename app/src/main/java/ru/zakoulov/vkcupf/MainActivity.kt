package ru.zakoulov.vkcupf

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.zakoulov.vkcupf.ui.welcome.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (VK.isLoggedIn()) {
//                navigateToPoster()
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
        VK.login(this, listOf(VKScope.WALL, VKScope.PHOTOS))
    }

    fun navigateToWelcome() = navigateTo(WelcomeFragment.instance)

//    fun navigateToPoster() = navigateTo(PosterFragment.instance)

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
//                navigateToPoster()
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
