package ru.zakoulov.vkcupd.data.models

data class Albums(
    val count: Int,
    val albums: List<Album>
)

data class Album(
    val id: Int,
    val title: String,
    val preview: String,
    val size: Int
)