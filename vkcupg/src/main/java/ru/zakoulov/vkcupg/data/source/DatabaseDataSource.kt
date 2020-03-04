package ru.zakoulov.vkcupg.data.source

import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.models.City

interface DatabaseDataSource {
    fun getCities(countryId: Int, callback: CommonResponseCallback<List<City>>)
}
