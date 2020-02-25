package ru.zakoulov.vkcupg.data

import ru.zakoulov.vkcupg.data.DatabaseRepository.Companion.COUNTRY_ID
import ru.zakoulov.vkcupg.data.core.LiveDataResponseCallback
import ru.zakoulov.vkcupg.data.core.SparseDataStorage
import ru.zakoulov.vkcupg.data.core.SparseDataStorageCallback
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.Markets
import ru.zakoulov.vkcupg.data.source.MarketsDataSource

class MarketsRepository(
    private val remoteSource: MarketsDataSource
) {

    private val markets = SparseDataStorage(object : SparseDataStorageCallback<Markets> {
        override fun initData(key: Int): Markets = Markets(0, emptyList())

        override fun fetchData(key: Int, data: StatusLiveData<Markets>) {
            // Already have all markets, don't DDoS VK
            data.data.let {
                if (it.count == it.markets.size) {
                    return
                }
            }
            remoteSource.fetchMarkets(
                countryId = COUNTRY_ID,
                cityId = key,
                count = NUM_OF_MARKETS_FOR_FETCHING,
                offset = data.data.markets.size,
                callback = LiveDataResponseCallback(data))
        }
    })

    fun getMarkets(cityId: Int): StatusLiveData<Markets> = markets[cityId]

    companion object {
        const val NUM_OF_MARKETS_FOR_FETCHING = 20
    }
}
