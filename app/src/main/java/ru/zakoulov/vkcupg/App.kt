package ru.zakoulov.vkcupg

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.zakoulov.vkcupg.data.MarketsRepository
import ru.zakoulov.vkcupg.data.ProductsRepository
import ru.zakoulov.vkcupg.data.source.vk.VkDatabaseDataSource
import ru.zakoulov.vkcupg.data.source.vk.VkMarketsDataSource
import ru.zakoulov.vkcupg.data.source.vk.VkProductsDataSource
import ru.zakoulov.vkcupg.data.source.vk.mappers.CitiesMapper
import ru.zakoulov.vkcupg.data.source.vk.mappers.MarketsMapper
import ru.zakoulov.vkcupg.data.source.vk.mappers.ProductsMapper

class App : Application() {

    lateinit var marketsRepository: MarketsRepository
    lateinit var productsRepository: ProductsRepository

    val tokenExpired = MutableLiveData<Boolean>(false)

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)

        val vkDatabaseDataSource = VkDatabaseDataSource(CitiesMapper())
        val vkMarketsDataSource = VkMarketsDataSource(MarketsMapper())
        val vkProductsDataSource = VkProductsDataSource(ProductsMapper())

        marketsRepository = MarketsRepository(vkMarketsDataSource, vkDatabaseDataSource)
        productsRepository = ProductsRepository(vkProductsDataSource)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = true
        }
    }
}
