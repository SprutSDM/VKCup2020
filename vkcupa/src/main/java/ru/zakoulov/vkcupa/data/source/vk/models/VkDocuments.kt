package ru.zakoulov.vkcupa.data.source.vk.models

import ru.zakoulov.vkcupa.data.source.vk.models.VkDocument

data class VkDocuments(
    val count: Int,
    val items: List<VkDocument>
)
