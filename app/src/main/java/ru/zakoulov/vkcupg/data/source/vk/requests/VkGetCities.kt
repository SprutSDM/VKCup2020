package ru.zakoulov.vkcupg.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.zakoulov.vkcupg.data.source.vk.models.VkCities
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarkets
import ru.zakoulov.vkcupg.vkutils.VKRequestGson

class VkGetCities(
    gson: Gson,
    jsonParser: JsonParser,
    countryId: Int
) : VKRequestGson<VkCities>(gson, jsonParser, "database.getCountries") {

    init {
        addParam("country_id", countryId)
    }

    override fun gsonParse(response: String): VkCities {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkCities::class.java)
    }
}
