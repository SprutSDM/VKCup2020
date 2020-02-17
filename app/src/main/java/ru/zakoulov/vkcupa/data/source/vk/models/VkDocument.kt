package ru.zakoulov.vkcupa.data.source.vk.models

data class VkDocument(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Int,
    val ext: String,
    val url: String,
    val date: Int,
    val type: Int,
    val preview: VkPreview,
    val tags: List<String>?
)
