package ru.zakoulov.vkcupd.ui.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.models.Album

class AlbumsViewAdapter(
    albums: List<Album>,
    private val callbacks: AlbumAdapterCallbacks
) : RecyclerView.Adapter<AlbumsViewAdapter.AlbumViewHolder>() {

    var albums: List<Album> = albums
        set(value) {
            field = value
            //TODO
            notifyDataSetChanged()
        }

    private var albumsState: AlbumsState = AlbumsState.OPEN

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val albumItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_item, parent, false) as View
        return AlbumViewHolder(albumItem)
    }

    override fun getItemCount() = albums.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.apply {
            setTitle(album.title)
            setSize(album.size)
            setPreview(album.preview, album.id)
            setAlbumState(albumsState, album.id)
        }
        holder.albumItem.setOnClickListener {
            callbacks.onAlbumClicked(album)
        }
        holder.albumItem.setOnLongClickListener {
            callbacks.onAlbumLongClicker(album)
            true
        }
        if (position + ALBUMS_BEFORE_LOAD_OFFSET > albums.size) {
            callbacks.fetchNewData()
        }
    }

    fun transformAlbumsToRemoving() {
        albumsState = AlbumsState.REMOVE
        notifyDataSetChanged()
    }

    fun transformAlbumsToOpening() {
        albumsState = AlbumsState.OPEN
        notifyDataSetChanged()
    }

    class AlbumViewHolder(val albumItem: View) : RecyclerView.ViewHolder(albumItem) {

        private var oldId = -1

        private val albumTitle: TextView = albumItem.findViewById(R.id.album_title)
        private val albumPreview: ImageView = albumItem.findViewById(R.id.album_preview)
        private val albumSize: TextView = albumItem.findViewById(R.id.album_size)
        private val albumRemoveIcon: ImageView = albumItem.findViewById(R.id.album_remove_icon)

        fun setTitle(title: String) {
            albumTitle.text = title
        }

        fun setSize(size: Int) {
            albumSize.text = albumItem.context.resources.getQuantityString(R.plurals.album_size, size, size)
        }

        fun setPreview(img: String, albumId: Int) {
            albumPreview.clipToOutline = true
            val requestCreator = Picasso.get()
                .load(img)
                .fit()
            if (oldId == albumId) {
                requestCreator.noFade()
            }
            oldId = albumId
            requestCreator.centerCrop()
                .into(albumPreview)
        }

        fun setAlbumState(albumsState: AlbumsState, albumId: Int) {
            when (albumsState) {
                AlbumsState.OPEN -> {
                    albumRemoveIcon.visibility = View.GONE
                    albumItem.alpha = 1f
                }
                AlbumsState.REMOVE -> {
                    if (albumId < 0) {
                        albumRemoveIcon.visibility = View.GONE
                        albumItem.alpha = 0.5f
                    } else {
                        albumItem.alpha = 1f
                        albumRemoveIcon.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    enum class AlbumsState {
        OPEN,
        REMOVE
    }

    companion object {
        const val ALBUMS_BEFORE_LOAD_OFFSET = 10
    }
}
