package ru.zakoulov.vkcupd.ui.albums

import ru.zakoulov.vkcupd.data.models.Album

interface AlbumAdapterCallbacks {
    fun fetchNewData()

    fun onAlbumClicked(album: Album)

    fun onAlbumLongClicker(album: Album)
}
