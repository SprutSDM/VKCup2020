package ru.zakoulov.vkcupf.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import ru.zakoulov.vkcupf.data.source.vk.models.VkGroup
import ru.zakoulov.vkcupf.data.source.vk.models.VkGroups
import java.lang.Exception

class VkGetGroupsCommand(
    private val gson: Gson,
    private val jsonParser: JsonParser
) : ApiCommand<List<VkGroup>>() {

    override fun onExecute(manager: VKApiManager): List<VkGroup> {
        val result = mutableListOf<VkGroup>()
        while (true) {
            val call = VKMethodCall.Builder()
                .method("groups.get")
                .args("count", CHUNK_LIMIT)
                .args("offset", result.size)
                .args("fields", "description,members_count")
                .args("extended", 1)
                .version(manager.config.version)
                .build()
            val callResult = manager.execute(call, ResponseApiParser(gson, jsonParser))
            result.addAll(callResult)
            if (callResult.size < CHUNK_LIMIT) {
                return result
            }
        }
    }

    private class ResponseApiParser(
        private val gson: Gson,
        private val jsonParser: JsonParser
    ) : VKApiResponseParser<List<VkGroup>> {

        override fun parse(response: String?): List<VkGroup> {
            try {
                return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkGroups::class.java).items
            } catch (e: Exception) {
                throw VKApiIllegalResponseException(e)
            }
        }
    }

    companion object {
        const val CHUNK_LIMIT = 9
    }
}
