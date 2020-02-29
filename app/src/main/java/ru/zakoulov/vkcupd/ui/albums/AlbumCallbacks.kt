package ru.zakoulov.vkcupd.ui.albums

import ru.zakoulov.vkcupd.data.models.Album

interface AlbumCallbacks {
    fun fetchNewData()

    fun showPhotosFromAlbum(album: Album)
}
