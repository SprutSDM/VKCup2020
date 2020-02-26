package ru.zakoulov.vkcupg.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarket
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarkets
import ru.zakoulov.vkcupg.data.source.vk.models.VkProduct
import ru.zakoulov.vkcupg.data.source.vk.models.VkProducts
import ru.zakoulov.vkcupg.vkutils.VKRequestGson

class VkGetProductsRequest(
    gson: Gson,
    jsonParser: JsonParser,
    marketId: Int,
    count: Int,
    offset: Int
) : VKRequestGson<VkProducts>(gson, jsonParser, "market.get") {

    init {
        addParam("owner_id", -marketId)
        addParam("count", count)
        addParam("offset", offset)
    }

    override fun gsonParse(response: String): VkProducts {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkProducts::class.java)
    }
}
