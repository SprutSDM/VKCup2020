package ru.zakoulov.vkcupg.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarket
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarkets
import ru.zakoulov.vkcupg.vkutils.VKRequestGson

class VkGetMarketsRequest(
    gson: Gson,
    jsonParser: JsonParser,
    countryId: Int,
    cityId: Int,
    count: Int,
    offset: Int
) : VKRequestGson<VkMarkets>(gson, jsonParser, "groups.search") {

    init {
        addParam("q", "*")
        addParam("country_id", countryId)
        addParam("city_id", cityId)
        addParam("market", 1)
        addParam("count", count)
        addParam("offset", offset)
    }

    override fun gsonParse(response: String): VkMarkets {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkMarkets::class.java)
    }
}
