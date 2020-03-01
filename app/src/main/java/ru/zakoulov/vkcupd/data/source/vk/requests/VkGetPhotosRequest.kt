package ru.zakoulov.vkcupd.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.zakoulov.vkcupd.data.source.vk.models.VkPhotos
import ru.zakoulov.vkcupd.vkutils.VKRequestGson

class VkGetPhotosRequest(
    gson: Gson,
    jsonParser: JsonParser,
    count: Int,
    offset: Int,
    albumId: Int
) : VKRequestGson<VkPhotos>(gson, jsonParser, "photos.get") {

    init {
        addParam("album_id", albumId)
        addParam("rev", 1)
        addParam("photo_sizes", 1)
        addParam("count", count)
        addParam("offset", offset)
    }

    override fun gsonParse(response: String): VkPhotos {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkPhotos::class.java)
    }
}
