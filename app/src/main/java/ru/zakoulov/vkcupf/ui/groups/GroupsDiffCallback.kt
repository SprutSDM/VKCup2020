package ru.zakoulov.vkcupf.ui.groups

import androidx.recyclerview.widget.DiffUtil

class GroupsDiffCallback(
    private val oldData: List<GroupsViewAdapter.GroupWrapper>,
    private val newData: List<GroupsViewAdapter.GroupWrapper>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].group.id == newData[newItemPosition].group.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].group == newData[newItemPosition].group
    }
}
