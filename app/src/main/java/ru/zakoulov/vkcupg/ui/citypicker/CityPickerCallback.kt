package ru.zakoulov.vkcupg.ui.citypicker

import ru.zakoulov.vkcupg.data.models.City

interface CityPickerCallback {
    fun pickCity(city: City)
}
