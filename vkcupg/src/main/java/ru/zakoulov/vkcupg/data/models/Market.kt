package ru.zakoulov.vkcupg.data.models

data class Market(
    val id: Int,
    val title: String,
    val photo: String,
    val isClosed: Boolean,
    val isMember: Boolean
)

data class Markets(
    val count: Int,
    val markets: List<Market>
)
