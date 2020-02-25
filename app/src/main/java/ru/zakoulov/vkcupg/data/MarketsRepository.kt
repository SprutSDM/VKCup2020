package ru.zakoulov.vkcupg.data

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import ru.zakoulov.vkcupg.data.DatabaseRepository.Companion.COUNTRY_ID
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.Markets
import ru.zakoulov.vkcupg.data.source.MarketsDataSource

class MarketsRepository(
    private val remoteSource: MarketsDataSource
) {

    private val markets = SparseArray<StatusLiveData<Markets>>()

    fun getMarkets(cityId: Int): StatusLiveData<Markets> {
        if (cityId in markets) {
            markets[cityId].let {
                if (it.isFailed()) {
                    it.setLoading()
                    fetchNewData(cityId)
                }
                return it
            }
        }
        val newData = StatusLiveData(RequestStatus.Empty(Markets(-1, emptyList())))
        markets[cityId] = newData
        fetchNewData(cityId)
        return newData
    }

    fun fetchNewData(cityId: Int, quiet: Boolean = false) {
        val cityMarkets = markets[cityId]
        if (cityMarkets.isLoading()) {
            return
        }
        // Already have all data. Don't DDoS VK
        if (cityMarkets.data.count == cityMarkets.data.markets.size) {
            return
        }
        cityMarkets.setLoading(quiet)
        remoteSource.fetchMarkets(COUNTRY_ID, cityId, NUM_OF_MARKETS_FOR_FETCHING, cityMarkets.data.markets.size,
            object : CommonResponseCallback<Markets> {
                override fun success(response: Markets) {
                    cityMarkets.value = RequestStatus.Success(Markets(
                        count = response.count,
                        markets = cityMarkets.data.markets + response.markets))
                }

                override fun fail(failMessage: String) {
                    markets[cityId].setFail(failMessage)
                }
            })
    }

    companion object {
        const val NUM_OF_MARKETS_FOR_FETCHING = 20
    }
}
