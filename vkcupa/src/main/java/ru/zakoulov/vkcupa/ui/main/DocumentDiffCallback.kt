package ru.zakoulov.vkcupa.ui.main

import androidx.recyclerview.widget.DiffUtil
import ru.zakoulov.vkcupa.data.Document

class DocumentDiffCallback(
    private val oldData: List<Document>,
    private val newData: List<Document>
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
