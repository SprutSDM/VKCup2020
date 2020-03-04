package ru.zakoulov.vkcupf.ui.groups

import androidx.recyclerview.widget.DiffUtil
import ru.zakoulov.vkcupf.data.Group

class GroupsDiffCallback(
    private val oldData: List<Group>,
    private val newData: List<Group>
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
