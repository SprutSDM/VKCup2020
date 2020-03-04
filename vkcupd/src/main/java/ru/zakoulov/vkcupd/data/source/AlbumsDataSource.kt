package ru.zakoulov.vkcupd.data.source

import android.net.Uri
import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.models.Photos

interface AlbumsDataSource {

    fun getAlbums(count: Int, offset: Int, callback: CommonResponseCallback<Albums>)

    fun getPhotos(albumId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Photos>)

    fun uploadPhoto(albumId: Int, photo: Uri, callback: CommonResponseCallback<Int>)

    fun createAlbum(albumTitle: String, callback: CommonResponseCallback<Int>)

    fun removeAlbum(albumId: Int, callback: CommonResponseCallback<Int>)
}
