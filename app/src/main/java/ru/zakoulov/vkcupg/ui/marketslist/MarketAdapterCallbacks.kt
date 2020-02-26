package ru.zakoulov.vkcupg.ui.marketslist

import ru.zakoulov.vkcupg.data.models.Market

interface MarketAdapterCallbacks {
    fun fetchNewData()
    fun navigateToProducts(market: Market)
}
