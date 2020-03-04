package ru.zakoulov.vkcupg.ui.productlist

import androidx.recyclerview.widget.DiffUtil
import ru.zakoulov.vkcupg.data.models.Product

class ProductsDiff(
    private val oldData: List<Product>,
    private val newData: List<Product>
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
