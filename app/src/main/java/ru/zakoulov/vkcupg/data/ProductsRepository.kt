package ru.zakoulov.vkcupg.data

import ru.zakoulov.vkcupg.data.core.LiveDataResponseCallback
import ru.zakoulov.vkcupg.data.core.SparseDataStorage
import ru.zakoulov.vkcupg.data.core.SparseDataStorageCallback
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.Products
import ru.zakoulov.vkcupg.data.source.ProductsDataSource

class ProductsRepository(
    private val remoteSource: ProductsDataSource
) {

    private val products = SparseDataStorage(object : SparseDataStorageCallback<Products> {
        override fun initData(key: Int) = Products(0, emptyList())

        override fun fetchData(key: Int, data: StatusLiveData<Products>) {
            // Already have all products, don't DDoS VK
            data.data.let {
                if (it.count == it.products.size) {
                    return
                }
            }
            remoteSource.fetchProducts(
                marketId = key,
                count = NUM_OF_PRODUCTS_FOR_FETCHING,
                offset = data.data.products.size,
                callback = LiveDataResponseCallback(data)
            )
        }
    })

    fun getProducts(marketId: Int) = products[marketId]

    companion object {
        const val NUM_OF_PRODUCTS_FOR_FETCHING = 20
    }
}
