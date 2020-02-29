package ru.zakoulov.vkcupd.data.models

data class Photos(
    val count: Int,
    val photos: List<Photo>
)

data class Photo(
    val id: Int,
    val src: String
)
