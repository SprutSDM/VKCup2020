package ru.zakoulov.vkcupd

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App : Application() {

//    lateinit var marketsRepository: MarketsRepository
//    lateinit var productsRepository: ProductsRepository
//    lateinit var favesRepository: FavesRepository

    val tokenExpired = MutableLiveData<Boolean>(false)

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)

        if (BuildConfig.DEBUG) {
            val builder = Picasso.Builder(this)
            builder.downloader(OkHttp3Downloader(this, Long.MAX_VALUE))
            val build = builder.build()
            build.setIndicatorsEnabled(true)
            build.isLoggingEnabled = true
            Picasso.setSingletonInstance(build)
        }

//        val productsMapper = ProductsMapper()
//
//        val vkDatabaseDataSource = VkDatabaseDataSource(CitiesMapper())
//        val vkMarketsDataSource = VkMarketsDataSource(MarketsMapper())
//        val vkProductsDataSource = VkProductsDataSource(productsMapper)
//        val vkFavesDataSource = VkFavesDataSource(productsMapper)
//
//        marketsRepository = MarketsRepository(vkMarketsDataSource, vkDatabaseDataSource)
//        productsRepository = ProductsRepository(vkProductsDataSource)
//        favesRepository = FavesRepository(vkFavesDataSource)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = true
        }
    }
}
