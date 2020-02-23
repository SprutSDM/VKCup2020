package ru.zakoulov.vkcupf.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.zakoulov.vkcupf.data.source.vk.models.VkPost
import ru.zakoulov.vkcupf.data.source.vk.models.VkWall
import ru.zakoulov.vkcupf.vkutils.VKRequestGson

class VkGetFirstPostRequest(
    gson: Gson,
    jsonParser: JsonParser,
    groupId: Int
) : VKRequestGson<VkPost?>(gson, jsonParser, "wall.get") {

    init {
        addParam("owner_id", -groupId)
        addParam("count", 1)
    }

    override fun gsonParse(response: String): VkPost? {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkWall::class.java).items.firstOrNull()
    }
}
