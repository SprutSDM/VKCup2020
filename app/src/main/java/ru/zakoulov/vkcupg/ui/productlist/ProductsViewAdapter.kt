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
    products: List<Product>
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
    }

    class ProductViewHolder(val productItem: View) : RecyclerView.ViewHolder(productItem) {

        private val productTitle: TextView = productItem.findViewById(R.id.product_title_info)
        private val productPhoto: ImageView = productItem.findViewById(R.id.product_photo_info)
        private val productPrice: TextView = productItem.findViewById(R.id.product_price_info)

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
}