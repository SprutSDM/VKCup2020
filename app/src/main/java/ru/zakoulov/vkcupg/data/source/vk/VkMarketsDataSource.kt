package ru.zakoulov.vkcupg.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.core.VKApiCallbackAdapter
import ru.zakoulov.vkcupg.data.models.Markets
import ru.zakoulov.vkcupg.data.source.MarketsDataSource
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarkets
import ru.zakoulov.vkcupg.data.source.vk.requests.VkGetMarketsRequest

class VkMarketsDataSource(
    private val marketsMapper: Mapper<VkMarkets, Markets>
) : MarketsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun fetchMarkets(countryId: Int, cityId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Markets>) {
        VK.execute(VkGetMarketsRequest(gson, jsonParser, countryId, cityId, count, offset),
            VKApiCallbackAdapter(callback, "Error fetching markets", marketsMapper))
    }
}
