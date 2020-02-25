package ru.zakoulov.vkcupg.data.models

data class Market(
    val title: String,
    val description: String
)

data class Markets(
    val count: Int,
    val items: List<Market>
)
