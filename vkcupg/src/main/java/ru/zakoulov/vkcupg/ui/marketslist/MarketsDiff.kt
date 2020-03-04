package ru.zakoulov.vkcupg.ui.marketslist

import androidx.recyclerview.widget.DiffUtil
import ru.zakoulov.vkcupg.data.models.Market

class MarketsDiff(
    private val oldData: List<Market>,
    private val newData: List<Market>
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
