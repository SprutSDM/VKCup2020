package ru.zakoulov.vkcupd.ui.albums.viewStrategy

import android.view.MenuItem
import ru.zakoulov.vkcupd.data.models.Album

class AlbumRemoveAlbumsStrategy : AlbumViewStrategy {

    private lateinit var callbacks: AlbumsStrategyCallbacks

    override fun setCallbacks(callbacks: AlbumsStrategyCallbacks) {
        this.callbacks = callbacks
    }

    override fun onAlbumClicked(album: Album) = callbacks.removeAlbum(album)
    override fun onAlbumLongClicked(album: Album) = Unit
    override fun onMenuItemClicked(menuItem: MenuItem) = false
    override fun onNavButtonClicked() = callbacks.showOpenPhotosInterface()
    override fun onBackPressed() = callbacks.showOpenPhotosInterface()
    override fun showInterface() = callbacks.showRemoveAlbumsInterface()
}
