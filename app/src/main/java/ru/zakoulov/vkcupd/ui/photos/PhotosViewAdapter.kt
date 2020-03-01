package ru.zakoulov.vkcupd.ui.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.models.Photo

class PhotosViewAdapter(
    photos: List<Photo>,
    private val callbacks: PhotoAdapterCallbacks
) : RecyclerView.Adapter<PhotosViewAdapter.PhotosViewHolder>() {

    var photos: List<Photo> = photos
        set(value) {
            field = value
            //TODO
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val photoItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false) as View
        return PhotosViewHolder(photoItem)
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = photos[position]
        holder.apply {
            setImg(photo.src)
        }
        if (position + PHOTOS_BEFORE_LOAD_OFFSET > photos.size) {
            callbacks.fetchNewData()
        }
    }

    class PhotosViewHolder(val photoItem: View) : RecyclerView.ViewHolder(photoItem) {

        private val photoImg: ImageView = photoItem.findViewById(R.id.photo_item)

        fun setImg(img: String) {
            Picasso.get()
                .load(img)
                .fit()
                .centerCrop()
                .into(photoImg)
        }
    }

    companion object {
        const val PHOTOS_BEFORE_LOAD_OFFSET = 20
    }
}
