package ru.zakoulov.vkcupg.ui.productinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.zakoulov.vkcupg.R

class ProductInfoFragment : Fragment() {

    private lateinit var butFavorite: Button
    private lateinit var productTitle: TextView
    private lateinit var productPhoto: ImageView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_info, container, false).apply {
            butFavorite = findViewById(R.id.but_favorites)
            productTitle = findViewById(R.id.product_title_info)
            productDescription = findViewById(R.id.product_description_info)
            productPhoto = findViewById(R.id.product_photo_info)
            productPrice = findViewById(R.id.product_price_info)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        val INSTANCE: ProductInfoFragment by lazy { ProductInfoFragment() }

        const val TAG = "ProductInfoFragment"
    }
}
