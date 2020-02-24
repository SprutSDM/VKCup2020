package ru.zakoulov.vkcupg.ui.shopslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.Shop

class ShopsViewAdapter(
    shops: List<Shop>
) : RecyclerView.Adapter<ShopsViewAdapter.ShopViewHolder>() {

    var shops: List<Shop> = shops
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_item, parent, false) as View
        return ShopViewHolder(documentView)
    }

    override fun getItemCount() = shops.size

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.apply {
            setTitle(shop.title)
            setDescription(shop.description)
        }
    }

    class ShopViewHolder(val shopItem: View) : RecyclerView.ViewHolder(shopItem) {

        private val shopTitle: TextView = shopItem.findViewById(R.id.shop_title)
        private val shopDescription: TextView = shopItem.findViewById(R.id.shop_description)

        fun setTitle(title: String) {
            shopTitle.text = title
        }

        fun setDescription(description: String) {
            shopDescription.text = description
        }
    }
}
