package ru.zakoulov.vkcupg.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.core.VKApiCallbackAdapter
import ru.zakoulov.vkcupg.data.models.City
import ru.zakoulov.vkcupg.data.source.DatabaseDataSource
import ru.zakoulov.vkcupg.data.source.vk.models.VkCities
import ru.zakoulov.vkcupg.data.source.vk.requests.VkGetCities

class VkDatabaseDataSource(
    private val citiesMapper: Mapper<VkCities, List<City>>
) : DatabaseDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getCities(countryId: Int, callback: CommonResponseCallback<List<City>>) {
        VK.execute(VkGetCities(gson, jsonParser, countryId),
            VKApiCallbackAdapter(callback, "Error fetching cities", citiesMapper))
    }
}
