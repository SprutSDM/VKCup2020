package ru.zakoulov.vkcupf.data.source.vk.requests

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

class VkLeaveGroupsCommand(
    private val groupsId: List<Int>
) : ApiCommand<List<Int>>() {

    override fun onExecute(manager: VKApiManager): List<Int> {
        val result = mutableListOf<Int>()
        for (groupId in groupsId) {
            val call = VKMethodCall.Builder()
                .method("groups.leave")
                .args("group_id", groupId)
                .version(manager.config.version)
                .build()
            val callResult = manager.execute(call, ResponseApiParser())
            if (callResult == 1) {
                result.add(groupId)
            }
        }
        return result
    }

    private class ResponseApiParser : VKApiResponseParser<Int> {
        override fun parse(response: String): Int {
            try {
                return JSONObject(response).getInt("response")
            } catch (e: Exception) {
                throw VKApiIllegalResponseException(e)
            }
        }
    }
}
