package ru.zakoulov.vkcupg.ui.productlist

import android.view.View
import android.widget.ImageView
import ru.zakoulov.vkcupg.data.models.Product

interface ProductAdapterCallbacks {
    fun fetchNewData()

    fun showMoreInfo(product: Product, sharedItem: View)
}
