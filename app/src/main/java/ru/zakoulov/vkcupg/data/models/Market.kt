package ru.zakoulov.vkcupg.data.models

data class Market(
    val title: String
)

data class Markets(
    val count: Int,
    val markets: List<Market>
)
