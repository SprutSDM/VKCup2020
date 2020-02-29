package ru.zakoulov.vkcupd.data.source

import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.models.Photos

interface AlbumsDataSource {

    fun getAlbums(count: Int, offset: Int, callback: CommonResponseCallback<Albums>)

    fun getPhotos(albumId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Photos>)
}
