package ru.zakoulov.vkcupg.ui.shopslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.Market

class ShopsViewAdapter(
    markets: List<Market>
) : RecyclerView.Adapter<ShopsViewAdapter.ShopViewHolder>() {

    var markets: List<Market> = markets
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_item, parent, false) as View
        return ShopViewHolder(documentView)
    }

    override fun getItemCount() = markets.size

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = markets[position]
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
