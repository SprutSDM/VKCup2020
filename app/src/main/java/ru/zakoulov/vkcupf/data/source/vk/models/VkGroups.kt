package ru.zakoulov.vkcupf.data.source.vk.models

data class VkGroups(
    val count: Int,
    val items: List<VkGroup>,
    val description: String
)
