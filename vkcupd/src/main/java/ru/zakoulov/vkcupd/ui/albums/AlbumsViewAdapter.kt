package ru.zakoulov.vkcupd.ui.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.models.Album

class AlbumsViewAdapter(
    albums: List<Album>,
    private val callbacks: AlbumAdapterCallbacks
) : RecyclerView.Adapter<AlbumsViewAdapter.AlbumViewHolder>() {

    init {
        setHasStableIds(true)
    }

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
            setPreview(album.preview, album.id, albumsState)
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


    override fun onViewAttachedToWindow(holder: AlbumViewHolder) {
        holder.animateAlbumPreview(albumsState)
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
        private var oldState: AlbumsState = AlbumsState.OPEN

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

        fun setPreview(img: String, albumId: Int, albumsState: AlbumsState) {
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
            animateAlbumPreview(albumsState, albumId)
        }

        fun setAlbumState(albumsState: AlbumsState, albumId: Int) {
            when (albumsState) {
                AlbumsState.OPEN -> {
                    albumRemoveIcon.visibility = View.GONE
                    if (shouldAnimate(albumsState, albumId)) {
                        animateRemoveIcon(false)
                    }
                    albumItem.alpha = 1f
                }
                AlbumsState.REMOVE -> {
                    if (albumId < 0) {
                        albumRemoveIcon.visibility = View.GONE
                        if (shouldAnimate(albumsState, albumId)) {
                            animateRemoveIcon(false)
                        }
                        albumItem.alpha = 0.5f
                    } else {
                        albumItem.alpha = 1f
                        albumRemoveIcon.visibility = View.VISIBLE
                        if (shouldAnimate(albumsState, albumId)) {
                            animateRemoveIcon(true)
                        }
                    }
                }
            }
            oldState = albumsState
        }

        private fun shouldAnimate(state: AlbumsState, albumId: Int) = oldState != state && albumId == oldId

        private fun animateRemoveIcon(active: Boolean) {
            val fromX = if (active) 0f else 1f
            val toX = if (active) 1f else 0f
            val fromY = if (active) 0f else 1f
            val toY = if (active) 1f else 0f
            val anim = ScaleAnimation(fromX, toX, fromY, toY,
                albumRemoveIcon.measuredWidth.toFloat() / 2, albumRemoveIcon.measuredHeight.toFloat() / 2)
            anim.duration = 200
            anim.interpolator = if (active) OvershootInterpolator() else FastOutLinearInInterpolator()
            albumRemoveIcon.startAnimation(anim)
        }

        fun animateAlbumPreview(state: AlbumsState, albumId: Int = oldId) {
            albumPreview.clearAnimation()

            if (state == AlbumsState.REMOVE && albumId > 0) {
                val animation = AnimationUtils.loadAnimation(albumPreview.context,
                    R.anim.trembling)
                albumPreview.startAnimation(animation)
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
