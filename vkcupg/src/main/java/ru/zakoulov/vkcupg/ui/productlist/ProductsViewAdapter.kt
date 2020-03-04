package ru.zakoulov.vkcupg.ui.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupg.MainActivity
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.Product

class ProductsViewAdapter(
    products: List<Product>,
    private val callbacks: ProductAdapterCallbacks,
    var imageWidth: Int
) : RecyclerView.Adapter<ProductsViewAdapter.ProductViewHolder>() {

    var products: List<Product> = products
        set(value) {
            val diff = ProductsDiff(field, value)
            val diffResult = DiffUtil.calculateDiff(diff)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false) as View

        return ProductViewHolder(documentView, imageWidth)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.apply {
            setTitle(product.title)
            setPrice(product.priceText)
            setPhoto(product.photo)
            setPhotoTransition(position)
        }
        holder.itemView.setOnClickListener {
            callbacks.showMoreInfo(product, holder.productPhoto)
        }
        if (position + PRODUCTS_LOAD_OFFSET > products.size) {
            callbacks.fetchNewData()
        }
    }

    class ProductViewHolder(productItem: View, private val imageWidth: Int) : RecyclerView.ViewHolder(productItem) {

        private val productTitle: TextView = productItem.findViewById(R.id.product_title)
        val productPhoto: ImageView = productItem.findViewById(R.id.product_photo)
        private val productPrice: TextView = productItem.findViewById(R.id.product_price)

        fun setTitle(title: String) {
            productTitle.text = title
        }

        fun setPrice(price: String) {
            productPrice.text = price
        }

        fun setPhoto(photo: String) {
            productPhoto.clipToOutline = true
            Picasso.get()
                .load(photo)
//                .fit()
                .resize(imageWidth, imageWidth)
                .into(productPhoto)
        }

        fun setPhotoTransition(position: Int) {
            productPhoto.transitionName = PHOTO_TRANSITION_NAME + position
        }

        companion object {
            const val PHOTO_TRANSITION_NAME = "product_photo_"
        }

    }

    companion object {
        const val PRODUCTS_LOAD_OFFSET = 10
    }
}
