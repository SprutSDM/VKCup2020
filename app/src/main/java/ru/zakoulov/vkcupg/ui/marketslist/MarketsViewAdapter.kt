package ru.zakoulov.vkcupg.ui.marketslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.models.Market

class MarketsViewAdapter(
    markets: List<Market>,
    private val callbacks: MarketAdapterCallbacks
) : RecyclerView.Adapter<MarketsViewAdapter.MarketViewHolder>() {

    var markets: List<Market> = markets
        set(value) {
            val diff = MarketsDiff(field, value)
            val diffResult = DiffUtil.calculateDiff(diff)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.market_item, parent, false) as View
        return MarketViewHolder(documentView)
    }

    override fun getItemCount() = markets.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = markets[position]
        holder.apply {
            setTitle(market.title)
            setDescription(market.isClosed)
            setIcon(market.photo)
        }
        holder.itemView.setOnClickListener {
            callbacks.navigateToProducts(market)
        }
        if (position + MARKETS_LOAD_OFFSET > markets.size) {
            callbacks.fetchNewData()
        }
    }

    class MarketViewHolder(private val marketItem: View) : RecyclerView.ViewHolder(marketItem) {

        private val marketTitle: TextView = marketItem.findViewById(R.id.market_title)
        private val marketDescription: TextView = marketItem.findViewById(R.id.market_description)
        private val marketIcon: ImageView = marketItem.findViewById(R.id.market_icon)

        fun setTitle(title: String) {
            marketTitle.text = title
        }

        fun setDescription(isClosed: Boolean) {
            marketDescription.text = marketItem.context.getString(
                if (isClosed) R.string.market_is_closed else R.string.market_is_opened)
        }

        fun setIcon(src: String) {
            marketIcon.clipToOutline = true
            Picasso.get()
                .load(src)
                .fit()
                .centerCrop()
                .into(marketIcon)
        }
    }

    companion object {
        const val MARKETS_LOAD_OFFSET = 10
    }
}
