package ru.zakoulov.vkcupe

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App : Application() {

    val tokenExpired = MutableLiveData<Int>()

    private var countOfTokenExpiration: Int = 0

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
        if (VK.isLoggedIn()) {

        }
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = ++countOfTokenExpiration
        }
    }
}
