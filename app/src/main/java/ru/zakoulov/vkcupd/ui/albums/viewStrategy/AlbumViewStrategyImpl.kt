package ru.zakoulov.vkcupd.ui.albums.viewStrategy

import android.view.MenuItem
import ru.zakoulov.vkcupd.data.models.Album

class AlbumViewStrategyImpl(
    private val openPhotosStrategy: AlbumViewStrategy,
    private val removeAlbumsStrategy: AlbumViewStrategy,
    private val albumsStrategyCallbacks: AlbumsStrategyCallbacks
) : AlbumViewStrategy, AlbumsStrategyCallbacks by albumsStrategyCallbacks {
    init {
        openPhotosStrategy.setCallbacks(this)
        removeAlbumsStrategy.setCallbacks(this)
    }

    private var currentStrategy = openPhotosStrategy

    override fun setCallbacks(callbacks: AlbumsStrategyCallbacks) = Unit
    override fun onAlbumClicked(album: Album) = currentStrategy.onAlbumClicked(album)
    override fun onAlbumLongClicked(album: Album) = currentStrategy.onAlbumLongClicked(album)
    override fun onMenuItemClicked(menuItem: MenuItem) = currentStrategy.onMenuItemClicked(menuItem)
    override fun onNavButtonClicked() = currentStrategy.onNavButtonClicked()
    override fun onBackPressed() = currentStrategy.onBackPressed()
    override fun showInterface() {
        currentStrategy.showInterface()
    }

    override fun showRemoveAlbumsInterface() {
        currentStrategy = removeAlbumsStrategy
        albumsStrategyCallbacks.showRemoveAlbumsInterface()
    }

    override fun showOpenPhotosInterface() {
        currentStrategy = openPhotosStrategy
        albumsStrategyCallbacks.showOpenPhotosInterface()
    }
}
