package ru.zakoulov.vkcupg.ui.productinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupg.App
import ru.zakoulov.vkcupg.MainActivity
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.FavesRepository
import ru.zakoulov.vkcupg.data.ProductsRepository
import ru.zakoulov.vkcupg.data.models.Product

class ProductInfoFragment : Fragment() {

    private lateinit var butFavorite: Button
    private lateinit var productTitle: TextView
    private lateinit var productPhoto: ImageView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView
    private lateinit var toolbar: Toolbar

    private lateinit var productsRepository: ProductsRepository
    private lateinit var favesRepository: FavesRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_product_info, container, false).apply {
            butFavorite = findViewById(R.id.but_favorites)
            productTitle = findViewById(R.id.product_title_info)
            productDescription = findViewById(R.id.product_description_info)
            productPhoto = findViewById(R.id.product_photo_info)
            productPrice = findViewById(R.id.product_price_info)
            toolbar = findViewById(R.id.toolbar)
        }
        arguments?.getString(KEY_TRANSITION_NAME)?.let { transitionName ->
            productPhoto.transitionName = transitionName
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_outline_28)
        (requireActivity() as MainActivity).apply {
            setSupportActionBar(toolbar)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as App).let {
            productsRepository = it.productsRepository
            favesRepository = it.favesRepository
        }

        toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        val marketId = arguments?.getInt(KEY_MARKET_ID) ?: return
        val productId = arguments?.getInt(KEY_PRODUCT_ID) ?: return

        favesRepository.requestNewProductFaveStatus(marketId, productId)
        favesRepository.getProductFaveStatus(marketId, productId).observe(viewLifecycleOwner) {
            setupFavoriteButton(it)
        }

        butFavorite.setOnClickListener {
            favesRepository.getProductFaveStatus(marketId, productId).value?.let {
                if (it) {
                    favesRepository.removeProductFromFaves(marketId, productId)
                } else {
                    favesRepository.addProductToFaves(marketId, productId)
                }
            }
        }

        val product = productsRepository.getProducts(marketId).data.products.find { it.id == productId} ?: return

        setFragmentTitle(product.title)
        setProductInfo(product)
    }

    private fun setProductInfo(product: Product) {
        productTitle.text = product.title
        productPrice.text = product.priceText
        productDescription.text = product.description
        productPhoto.clipToOutline = true
        val imageWidth = getImageWidth()
        Picasso.get()
            .load(product.photo)
            .resize(imageWidth, imageWidth)
            .into(productPhoto)
    }

    private fun getImageWidth() = (requireActivity() as MainActivity).getScreenWidth() / 2

    private fun setFragmentTitle(title: String) {
        requireActivity().title = title
    }

    private fun setupFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            butFavorite.setBackgroundResource(R.drawable.shape_button_gray)
            butFavorite.setText(R.string.fave_remove)
        } else {
            butFavorite.setBackgroundResource(R.drawable.shape_button)
            butFavorite.setText(R.string.fave_add)
        }
    }

    companion object {
        const val TAG = "ProductInfoFragment"

        const val KEY_MARKET_ID = "key_market_id"
        const val KEY_PRODUCT_ID = "key_product_id"
        const val KEY_TRANSITION_NAME = "key_transition_name"
    }
}
