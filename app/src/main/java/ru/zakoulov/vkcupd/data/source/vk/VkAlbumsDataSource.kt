package ru.zakoulov.vkcupd.data.source.vk

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.core.IntMapper
import ru.zakoulov.vkcupd.data.core.Mapper
import ru.zakoulov.vkcupd.data.core.VKApiCallbackAdapter
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.models.Photos
import ru.zakoulov.vkcupd.data.source.AlbumsDataSource
import ru.zakoulov.vkcupd.data.source.vk.models.VkAlbums
import ru.zakoulov.vkcupd.data.source.vk.models.VkPhotos
import ru.zakoulov.vkcupd.data.source.vk.requests.VkCreateAlbumRequest
import ru.zakoulov.vkcupd.data.source.vk.requests.VkGetAlbumsRequest
import ru.zakoulov.vkcupd.data.source.vk.requests.VkGetPhotosRequest
import ru.zakoulov.vkcupd.data.source.vk.requests.VkPhotoPostCommand

class VkAlbumsDataSource(
    private val albumsMapper: Mapper<VkAlbums, Albums>,
    private val photosMapper: Mapper<VkPhotos, Photos>
) : AlbumsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getAlbums(count: Int, offset: Int, callback: CommonResponseCallback<Albums>) {
        VK.execute(VkGetAlbumsRequest(gson, jsonParser, count, offset),
            VKApiCallbackAdapter(callback, "Error fetching albums", albumsMapper))
    }

    override fun getPhotos(albumId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Photos>) {
        VK.execute(VkGetPhotosRequest(gson, jsonParser, count, offset, albumId),
            VKApiCallbackAdapter(callback, "Error fetching photos", photosMapper))
    }

    override fun uploadPhoto(albumId: Int, photo: Uri, callback: CommonResponseCallback<Int>) {
        VK.execute(VkPhotoPostCommand(albumId, listOf(photo)),
            VKApiCallbackAdapter(callback, "Error uploading photo", IntMapper))
    }

    override fun createAlbum(albumTitle: String, callback: CommonResponseCallback<Int>) {
        VK.execute(VkCreateAlbumRequest(albumTitle),
            VKApiCallbackAdapter(callback, "Error creating new album", IntMapper))
    }
}
