package ru.zakoulov.vkcupd.ui.albums.viewStrategy

import android.view.MenuItem
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.models.Album

class AlbumOpenPhotosStrategy : AlbumViewStrategy {

    private lateinit var callbacks: AlbumsStrategyCallbacks

    override fun setCallbacks(callbacks: AlbumsStrategyCallbacks) {
        this.callbacks = callbacks
    }

    override fun onAlbumClicked(album: Album) = callbacks.openPhotos(album)
    override fun onAlbumLongClicked(album: Album) = callbacks.showRemoveAlbumsInterface()

    override fun onMenuItemClicked(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add -> {
                callbacks.createAlbum()
                true
            }
            else -> false
        }
    }

    override fun onNavButtonClicked() = Unit
    override fun onBackPressed() = false
    override fun showInterface() = callbacks.showOpenPhotosInterface()
}
