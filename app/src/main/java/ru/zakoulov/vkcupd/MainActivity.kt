package ru.zakoulov.vkcupd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.zakoulov.vkcupd.ui.albums.AlbumsFragment
import ru.zakoulov.vkcupd.ui.photos.PhotosFragment
import ru.zakoulov.vkcupd.ui.welcome.WelcomeFragment

class MainActivity : AppCompatActivity() {

    private var shouldNavigateAfterOnActivityResult: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (VK.isLoggedIn()) {
                App.getApp(application).albumsRepository.fetchNewAlbums()
                navigateToAlbums()
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
            navigateToAlbums()
        }
    }

    fun login() {
        VK.login(this, listOf(VKScope.PHOTOS))
    }

    fun navigateToWelcome() = navigateTo(WelcomeFragment.instance, WelcomeFragment.TAG)

    fun navigateToAlbums() = navigateTo(AlbumsFragment.instance, AlbumsFragment.TAG)

    fun navigateToAlbumPhotos(albumId: Int) {
        val fragment = PhotosFragment()
        fragment.arguments = Bundle().apply {
            putInt(PhotosFragment.KEY_ALBUM_ID, albumId)
        }
        navigateTo(fragment, PhotosFragment.TAG, addToBackStack = true)
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
                App.getApp(this@MainActivity.application).tokenExpired.value = false
                shouldNavigateAfterOnActivityResult = true
                App.getApp(this@MainActivity.application).albumsRepository.fetchNewAlbums()
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        fun getActivity(activity: Activity): MainActivity {
            return activity as MainActivity
        }
    }
}
