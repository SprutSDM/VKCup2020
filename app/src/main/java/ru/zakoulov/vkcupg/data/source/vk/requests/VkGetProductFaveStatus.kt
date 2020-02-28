package ru.zakoulov.vkcupg.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.zakoulov.vkcupg.data.source.vk.models.VkProducts
import ru.zakoulov.vkcupg.vkutils.VKRequestGson

class VkGetProductFaveStatus(
    gson: Gson,
    jsonParser: JsonParser,
    marketId: Int,
    productId: Int
) : VKRequestGson<VkProducts>(gson, jsonParser, "market.getById") {

    init {
        addParam("item_ids", "${-marketId}_${productId}")
    }

    override fun gsonParse(response: String): VkProducts {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkProducts::class.java)
    }
}
