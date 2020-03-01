package ru.zakoulov.vkcupd.ui.albums.viewStrategy

import ru.zakoulov.vkcupd.data.models.Album

interface AlbumsStrategyCallbacks {
    fun openPhotos(album: Album)
    fun removeAlbum(album: Album)
    fun createAlbum()
    fun editAlbum()
    fun showRemoveAlbumsInterface()
    fun showOpenPhotosInterface()
}
