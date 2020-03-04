package ru.zakoulov.vkcupg.data.source

import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.models.Products

interface ProductsDataSource {
    fun fetchProducts(marketId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Products>)
}
