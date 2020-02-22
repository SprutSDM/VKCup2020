package ru.zakoulov.vkcupf.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.zakoulov.vkcupf.data.source.vk.models.VkGroups
import ru.zakoulov.vkcupf.vkutils.VKRequestGson

class VkGetGroupsRequest(
    gson: Gson,
    jsonParser: JsonParser,
    count: Int,
    offset: Int
) : VKRequestGson<VkGroups>(gson, jsonParser, "groups.get") {

    init {
        addParam("count", count)
        addParam("offset", offset)
        addParam("fields", "description")
    }

    override fun gsonParse(response: String): VkGroups {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkGroups::class.java)
    }
}
