package ru.zakoulov.vkcupd.ui.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupd.R

class AlbumsViewAdatper(
    albums: List<Any>
) : RecyclerView.Adapter<AlbumsViewAdatper.AlbumViewHolder>() {

    var albums: List<Any> = albums
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
        album.apply {

        }


    }

    class AlbumViewHolder(val albumItem: View) : RecyclerView.ViewHolder(albumItem) {

    }
}
