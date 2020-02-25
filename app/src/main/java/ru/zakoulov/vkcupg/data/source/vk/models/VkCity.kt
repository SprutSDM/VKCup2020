package ru.zakoulov.vkcupg.data.source.vk.models

data class VkCity(
    val id: Int,
    val title: String
)

data class VkCities(
    val count: Int,
    val items: List<VkCity>
)
