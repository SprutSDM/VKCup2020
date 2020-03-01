package ru.zakoulov.vkcupd.ui.photos

import androidx.recyclerview.widget.DiffUtil
import ru.zakoulov.vkcupd.data.models.Photo

class PhotosDiffCallbacks(
    private val oldData: List<Photo>,
    private val newData: List<Photo>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}
