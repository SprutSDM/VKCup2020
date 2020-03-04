package ru.zakoulov.vkcupd.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.zakoulov.vkcupd.data.source.vk.models.VkAlbums
import ru.zakoulov.vkcupd.vkutils.VKRequestGson

class VkGetAlbumsRequest(
    gson: Gson,
    jsonParser: JsonParser,
    count: Int,
    offset: Int
) : VKRequestGson<VkAlbums>(gson, jsonParser, "photos.getAlbums") {

    init {
        addParam("need_system", 1)
        addParam("need_covers", 1)
        addParam("photo_sizes", 1)
        addParam("count", count)
        addParam("offset", offset)
    }

    override fun gsonParse(response: String): VkAlbums {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkAlbums::class.java)
    }
}
