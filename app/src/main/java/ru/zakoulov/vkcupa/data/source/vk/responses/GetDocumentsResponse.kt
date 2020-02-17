package ru.zakoulov.vkcupa.data.source.vk.responses

import ru.zakoulov.vkcupa.data.source.vk.models.VkDocument

data class GetDocumentsResponse(
    val count: Int,
    val items: List<VkDocument>
)
