package ru.zakoulov.vkcupf

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.zakoulov.vkcupf.data.GroupRepository
import ru.zakoulov.vkcupf.data.WallRepository
import ru.zakoulov.vkcupf.data.source.vk.VkGroupsDataSource
import ru.zakoulov.vkcupf.data.source.vk.VkWallDataSource
import ru.zakoulov.vkcupf.data.source.vk.mappers.GroupMapper

class App : Application() {

    lateinit var groupRepository: GroupRepository

    val tokenExpired = MutableLiveData<Boolean>(false)

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
        groupRepository = GroupRepository(VkGroupsDataSource(GroupMapper()), WallRepository(VkWallDataSource()))
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = true
        }
    }
}
