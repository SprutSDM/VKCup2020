package ru.zakoulov.vkcupd.data.source.vk.models

data class VkPhotos(
    val count: Int,
    val items: List<VkPhoto>
)

data class VkPhoto(
    val id: Int,
    val sizes: List<VkPhotoProps>
)

data class VkPhotoProps(
    val type: String,
    val url: String
)
