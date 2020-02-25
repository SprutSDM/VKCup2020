package ru.zakoulov.vkcupg.data

import ru.zakoulov.vkcupg.data.core.LiveDataResponseCallback
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.City
import ru.zakoulov.vkcupg.data.source.DatabaseDataSource

class DatabaseRepository(
    private val remoteSource: DatabaseDataSource
) {
    private val cities = StatusLiveData<List<City>>(RequestStatus.Empty(emptyList()))

    fun getCities(): StatusLiveData<List<City>> {
        if (cities.isFailed() || cities.isEmpty()) {
            cities.setLoading()
            remoteSource.getCities(COUNTRY_ID, LiveDataResponseCallback(cities))
        }
        return cities
    }

    companion object {
        const val COUNTRY_ID = 1
    }
}
