package ru.zakoulov.vkcupg.data.source.vk.mappers

import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.models.City
import ru.zakoulov.vkcupg.data.source.vk.models.VkCities

class CitiesMapper : Mapper<VkCities, List<City>> {
    override fun map(input: VkCities): List<City> {
        return input.items.map {
            City(
                id = it.id,
                name = it.title
            )
        }
    }
}
