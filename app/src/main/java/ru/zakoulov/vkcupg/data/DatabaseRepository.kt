package ru.zakoulov.vkcupg.data

import ru.zakoulov.vkcupg.data.core.ReplaceLiveDataResponseCallback
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.City
import ru.zakoulov.vkcupg.data.source.DatabaseDataSource

class DatabaseRepository(
    private val remoteSource: DatabaseDataSource
) {
    private val cities = StatusLiveData<List<City>>(RequestStatus.Empty(emptyList()))

    var city: City? = null

    fun getCurrentCity(): City {
        return city ?: cities.data[0]
    }

    fun setCurrentCity(city: City) {
        this.city = city
    }

    fun getCities(): StatusLiveData<List<City>> {
        if (cities.isFailed() || cities.isEmpty()) {
            cities.setLoading()
            remoteSource.getCities(COUNTRY_ID, ReplaceLiveDataResponseCallback(cities))
        }
        return cities
    }

    companion object {
        const val COUNTRY_ID = 1
    }
}
