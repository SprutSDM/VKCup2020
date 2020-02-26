package ru.zakoulov.vkcupg.ui.productlist

import ru.zakoulov.vkcupg.data.models.Product

interface ProductAdapterCallbacks {
    fun fetchNewData()

    fun showMoreInfo(product: Product)
}
