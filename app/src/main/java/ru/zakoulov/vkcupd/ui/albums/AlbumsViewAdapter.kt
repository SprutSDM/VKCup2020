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
    private val callbacks: AlbumCallbacks
) : RecyclerView.Adapter<AlbumsViewAdapter.AlbumViewHolder>() {

    var albums: List<Album> = albums
        set(value) {
            field = value
            //TODO
            notifyDataSetChanged()
        }

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
            setPreview(album.preview)
        }
        holder.albumItem.setOnClickListener {
            callbacks.showPhotosFromAlbum(album)
        }
        if (position + ALBUMS_BEFORE_LOAD_OFFSET > albums.size) {
            callbacks.fetchNewData()
        }
    }

    class AlbumViewHolder(val albumItem: View) : RecyclerView.ViewHolder(albumItem) {

        private val albumTitle: TextView = albumItem.findViewById(R.id.album_title)
        private val albumPreview: ImageView = albumItem.findViewById(R.id.album_preview)
        private val albumSize: TextView = albumItem.findViewById(R.id.album_size)

        fun setTitle(title: String) {
            albumTitle.text = title
        }

        fun setSize(size: Int) {
            albumSize.text = albumItem.context.resources.getQuantityString(R.plurals.album_size, size, size)
        }

        fun setPreview(img: String) {
            albumPreview.clipToOutline = true
            Picasso.get()
                .load(img)
                .fit()
                .centerCrop()
                .into(albumPreview)
        }
    }

    companion object {
        const val ALBUMS_BEFORE_LOAD_OFFSET = 10
    }
}
