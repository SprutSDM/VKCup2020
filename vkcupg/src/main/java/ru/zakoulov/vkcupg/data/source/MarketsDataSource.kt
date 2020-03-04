package ru.zakoulov.vkcupg.data.source

import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.models.Markets

interface MarketsDataSource {
    fun fetchMarkets(countryId: Int, cityId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Markets>)
}
