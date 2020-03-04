package ru.zakoulov.vkcupe

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.zakoulov.vkcupe.data.PostsRepository
import ru.zakoulov.vkcupe.data.source.vk.VkPostsDataSource

class App : Application() {

    lateinit var postsRepository: PostsRepository

    val tokenExpired = MutableLiveData<Int>()

    private var countOfTokenExpiration: Int = 0

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
        postsRepository = PostsRepository(VkPostsDataSource())
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = ++countOfTokenExpiration
        }
    }
}
