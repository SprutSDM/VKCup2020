package ru.zakoulov.vkcupg.ui.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.Product

class ProductsViewAdapter(
    products: List<Product>,
    private val callbacks: ProductAdapterCallbacks
) : RecyclerView.Adapter<ProductsViewAdapter.ProductViewHolder>() {

    var products: List<Product> = products
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false) as View
        return ProductViewHolder(documentView)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.apply {
            setTitle(product.title)
            setPrice(product.priceText)
            setPhoto(product.photo)
        }
        holder.itemView.setOnClickListener {
            callbacks.showMoreInfo(product)
        }
        if (position + PRODUCTS_LOAD_OFFSET > products.size) {
            callbacks.fetchNewData()
        }
    }

    class ProductViewHolder(productItem: View) : RecyclerView.ViewHolder(productItem) {

        private val productTitle: TextView = productItem.findViewById(R.id.product_title)
        private val productPhoto: ImageView = productItem.findViewById(R.id.product_photo)
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
                .fit()
                .centerCrop()
                .into(productPhoto)
        }
    }
    companion object {
        const val PRODUCTS_LOAD_OFFSET = 10
    }
}
