package ru.zakoulov.vkcupd

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.zakoulov.vkcupd.data.AlbumsRepository
import ru.zakoulov.vkcupd.data.source.mock.MockAlbumDataSource

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

        val albumsDataSource = MockAlbumDataSource()

        albumsRepository = AlbumsRepository(albumsDataSource)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = true
        }
    }

    companion object {
        fun getApp(context: Context): App {
            return context as App
        }
    }
}
