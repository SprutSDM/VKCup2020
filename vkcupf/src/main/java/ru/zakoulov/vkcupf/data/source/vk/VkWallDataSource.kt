package ru.zakoulov.vkcupf.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.WallDataSource
import ru.zakoulov.vkcupf.data.source.vk.models.VkPost
import ru.zakoulov.vkcupf.data.source.vk.requests.VkGetFirstPostRequest

class VkWallDataSource : WallDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getDateOfFirstPost(groupId: Int, callback: CommonResponseCallback<Long>) {
        VK.execute(VkGetFirstPostRequest(gson, jsonParser, groupId), object : VKApiCallback<VkPost> {
            override fun success(result: VkPost) {
                callback.success(result.date)
            }

            override fun fail(error: Exception) {
                callback.fail(error.localizedMessage ?: error.message ?: "Error getting date of first post.")
            }
        })
    }
}
