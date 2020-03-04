package ru.zakoulov.vkcupd.data.source.vk.models

data class VkAlbums(
    val count: Int,
    val items: List<VkAlbum>
)

data class VkAlbum(
    val id: Int,
    val title: String,
    val size: Int,
    val sizes: List<VkAlbumPhotoProps>
)

data class VkAlbumPhotoProps(
    val src: String,
    val type: String
)
