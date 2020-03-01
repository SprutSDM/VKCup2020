package ru.zakoulov.vkcupd.ui.albums.viewStrategy

import android.view.MenuItem
import ru.zakoulov.vkcupd.data.models.Album

interface AlbumViewStrategy {
    fun setCallbacks(callbacks: AlbumsStrategyCallbacks)

    fun onAlbumClicked(album: Album)
    fun onAlbumLongClicked(album: Album)
    fun onMenuItemClicked(menuItem: MenuItem): Boolean
    fun onNavButtonClicked()
    fun onBackPressed()
    fun showInterface()
}
