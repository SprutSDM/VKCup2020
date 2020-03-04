package ru.zakoulov.vkcupg.data

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.LiveDataResponseCallback
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.City
import ru.zakoulov.vkcupg.data.models.Markets
import ru.zakoulov.vkcupg.data.source.DatabaseDataSource
import ru.zakoulov.vkcupg.data.source.MarketsDataSource

class MarketsRepository(
    private val remoteSource: MarketsDataSource,
    private val databaseRemoteSource: DatabaseDataSource
) {

    val cities = StatusLiveData<List<City>>(RequestStatus.Empty(emptyList()))
    val currentCity = StatusLiveData(RequestStatus.Empty(City(-1, "")))

    private val markets = SparseArray<StatusLiveData<Markets>>()
    val currentMarkets = StatusLiveData(RequestStatus.Empty(Markets(-1, emptyList())))

    private fun setMarketsForCity(city: City) {
        if (city.id in markets) {
            markets[city.id].let {
                if (it.isFailed() || it.isEmpty()) {
                    fetchNewData()
                }
                currentMarkets.value = markets[city.id].value
                return
            }
        }
        val newData = StatusLiveData(RequestStatus.Empty(Markets(-1, emptyList())))
        markets[city.id] = newData
        fetchNewData()
        currentMarkets.value = newData.value
    }

    fun fetchNewData(quiet: Boolean = false) {
        val city = currentCity.value?.data ?: return
        val cityMarkets = markets[city.id]
        if (cityMarkets.isLoading()) {
            return
        }
        // Already have all data. Don't DDoS VK
        if (cityMarkets.data.count == cityMarkets.data.markets.size) {
            return
        }
        cityMarkets.setLoading(quiet)
        marketsDataChanged(city)
        remoteSource.fetchMarkets(COUNTRY_ID, city.id, NUM_OF_MARKETS_FOR_FETCHING, cityMarkets.data.markets.size,
            object : CommonResponseCallback<Markets> {
                override fun success(response: Markets) {

                    cityMarkets.value = RequestStatus.Success(Markets(
                        count = response.count,
                        markets = cityMarkets.data.markets + response.markets))
                    marketsDataChanged(city)
                }

                override fun fail(failMessage: String) {
                    cityMarkets.setFail(failMessage)
                    marketsDataChanged(city)
                }
            })
    }

    fun setCurrentCity(city: City) {
        currentCity.value = RequestStatus.Success(city)
        setMarketsForCity(city)
    }

    fun fetchCities() {
        if (cities.isFailed() || cities.isEmpty()) {
            cities.setLoading()
            databaseRemoteSource.getCities(COUNTRY_ID, LiveDataResponseCallback(cities) {
                setCurrentCity(it[0])
            })
        }
    }

    private fun marketsDataChanged(city: City) {
        if (currentCity.value?.data?.id == city.id) {
            currentMarkets.value = markets[city.id].value
        }
    }

    companion object {
        const val NUM_OF_MARKETS_FOR_FETCHING = 20
        const val COUNTRY_ID = 1
    }
}
