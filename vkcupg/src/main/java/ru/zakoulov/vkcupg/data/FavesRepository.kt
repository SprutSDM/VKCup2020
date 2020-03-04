package ru.zakoulov.vkcupg.data

import androidx.lifecycle.MutableLiveData
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.models.Products
import ru.zakoulov.vkcupg.data.source.FavesDataSource

class FavesRepository(private val remoteSource: FavesDataSource) {

    private val faves = HashMap<Pair<Int, Int>, MutableLiveData<Boolean>>()
    private var timeFromLastRequest = 0L

    fun addProductToFaves(marketId: Int, productId: Int) {
        if (System.currentTimeMillis() - timeFromLastRequest < 350f) {
            return
        }
        val faveStatus = getProductFaveStatus(marketId, productId)
        timeFromLastRequest = System.currentTimeMillis()
        remoteSource.addProduct(marketId, productId, object : CommonResponseCallback<Int> {
            override fun success(response: Int) {
                if (response == 1) {
                    faveStatus.value = true
                }
            }

            override fun fail(failMessage: String) {
            }
        })
    }

    fun removeProductFromFaves(marketId: Int, productId: Int) {
        val faveStatus = getProductFaveStatus(marketId, productId)
        if (System.currentTimeMillis() - timeFromLastRequest < 350f) {
            return
        }
        timeFromLastRequest = System.currentTimeMillis()
        remoteSource.removeProduct(marketId, productId, object : CommonResponseCallback<Int> {
            override fun success(response: Int) {
                if (response == 1) {
                    faveStatus.value = false
                }
            }

            override fun fail(failMessage: String) {
            }
        })
    }

    fun getProductFaveStatus(marketId: Int, productId: Int): MutableLiveData<Boolean> {
        val key = Pair(marketId, productId)
        if (key !in faves) {
            faves[key] = MutableLiveData(false)

        }
        return faves[key]!!
    }

    fun requestNewProductFaveStatus(groupId: Int, productId: Int) {
        val faveStatus = getProductFaveStatus(groupId, productId)
        remoteSource.getProductFaveStatus(groupId, productId, object : CommonResponseCallback<Products> {
            override fun success(response: Products) {
                response.products.firstOrNull()?.let {
                    if (it.id == productId) {
                        faveStatus.value = it.isFavorite
                    }
                }
            }

            override fun fail(failMessage: String) {
            }
        })
    }
}
