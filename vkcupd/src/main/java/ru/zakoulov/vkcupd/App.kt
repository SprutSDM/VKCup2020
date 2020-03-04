package ru.zakoulov.vkcupd

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.zakoulov.vkcupd.data.AlbumsRepository
import ru.zakoulov.vkcupd.data.source.vk.VkAlbumsDataSource
import ru.zakoulov.vkcupd.data.source.vk.mappers.AlbumsMapper
import ru.zakoulov.vkcupd.data.source.vk.mappers.PhotosMapper

class App : Application() {

    lateinit var albumsRepository: AlbumsRepository

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

        val albumsDataSource = VkAlbumsDataSource(AlbumsMapper(), PhotosMapper())

        albumsRepository = AlbumsRepository(albumsDataSource)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = true
        }
    }

    companion object {
        fun getApp(application: Application): App {
            return application as App
        }
    }
}
