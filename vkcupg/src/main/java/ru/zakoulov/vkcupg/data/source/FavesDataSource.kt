package ru.zakoulov.vkcupg.data.source

import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.models.Products

interface FavesDataSource {
    fun addProduct(marketId: Int, productId: Int, callback: CommonResponseCallback<Int>)

    fun removeProduct(marketId: Int, productId: Int, callback: CommonResponseCallback<Int>)

    fun getProductFaveStatus(marketId: Int, productId: Int, callback: CommonResponseCallback<Products>)
}
