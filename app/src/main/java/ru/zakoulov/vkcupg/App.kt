package ru.zakoulov.vkcupg

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App : Application() {

//    lateinit var groupRepository: GroupRepository

    val tokenExpired = MutableLiveData<Boolean>(false)

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
//        groupRepository = GroupRepository(VkGroupsDataSource(GroupMapper()), WallRepository(VkWallDataSource()))
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = true
        }
    }
}
