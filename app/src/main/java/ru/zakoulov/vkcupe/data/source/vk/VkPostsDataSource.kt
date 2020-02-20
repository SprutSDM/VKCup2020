package ru.zakoulov.vkcupe.data.source.vk

import android.net.Uri
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupe.data.source.CommonResponseCallback
import ru.zakoulov.vkcupe.data.source.PostsDataSource
import ru.zakoulov.vkcupe.data.source.vk.requests.VKWallPostCommand

class VkPostsDataSource : PostsDataSource {
    override fun addPost(message: String?, photos: List<Uri>, callback: CommonResponseCallback<Int>) {
        VK.execute(VKWallPostCommand(message = message, photos = photos), object: VKApiCallback<Int> {
            override fun success(result: Int) {
                 callback.success(result)
            }

            override fun fail(error: Exception) {
                callback.fail("Error upload post")
            }
        })
    }
}
