package ru.zakoulov.vkcupa.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.zakoulov.vkcupa.data.source.vk.responses.DocumentsResponse
import ru.zakoulov.vkcupa.vkutils.VKRequestGson

class VkDocumentsRequest(
    gson: Gson,
    jsonParser: JsonParser,
    count: Int = 1,
    offset: Int = 0,
    return_tags: Boolean = true
) : VKRequestGson<DocumentsResponse>(gson, jsonParser, "docs.get") {
    init {
        addParam("count", count)
        addParam("offset", offset)
        addParam("return_tags", if (return_tags) 1 else 0)
    }

    override fun gsonParse(response: String): DocumentsResponse {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], DocumentsResponse::class.java)
    }
}
