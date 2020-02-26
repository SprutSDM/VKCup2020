package ru.zakoulov.vkcupg.data

import android.util.Log
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.core.SparseStorage
import ru.zakoulov.vkcupg.data.core.SparseStorageCallback
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.Products
import ru.zakoulov.vkcupg.data.source.ProductsDataSource

class ProductsRepository(
    private val remoteSource: ProductsDataSource
) {

    private val products = SparseStorage(object : SparseStorageCallback<Products> {
        override fun initData(key: Int) = Products(-1, emptyList())

        override fun fetchData(key: Int, data: StatusLiveData<Products>, quiet: Boolean) {
            if (data.isLoading()) {
                return
            }
            // Already have all products, don't DDoS VK
            data.data.let {
                if (it.count == it.products.size) {
                    return
                }
            }
            data.setLoading(quiet)
            remoteSource.fetchProducts(
                marketId = key,
                count = NUM_OF_PRODUCTS_FOR_FETCHING,
                offset = data.data.products.size,
                callback = object : CommonResponseCallback<Products> {
                    override fun success(response: Products) {
                        data.value = RequestStatus.Success(Products(
                            response.count,
                            data.data.products + response.products))
                    }

                    override fun fail(failMessage: String) {
                        data.value = RequestStatus.Fail(data.data, failMessage)
                    }
                }
            )
        }
    })

    fun getProducts(marketId: Int) = products[marketId]

    fun fetchNewData(marketId: Int, quiet: Boolean = false) {
        products.fetchNewData(marketId, quiet)
    }

    companion object {
        const val NUM_OF_PRODUCTS_FOR_FETCHING = 20
    }
}
