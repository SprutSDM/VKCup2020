package ru.zakoulov.vkcupg.ui.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import ru.zakoulov.vkcupg.App
import ru.zakoulov.vkcupg.MainActivity
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.MarketsRepository
import ru.zakoulov.vkcupg.data.ProductsRepository
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.models.Product

class ProductsListFragment : Fragment(), ProductAdapterCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var butReload: Button
    private lateinit var errorContainer: View
    private lateinit var toolbar: Toolbar

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: ProductsViewAdapter
    private var marketId: Int = 0

    private lateinit var marketsRepository: MarketsRepository
    private lateinit var productsRepository: ProductsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view_products)
            progressBar = findViewById(R.id.progress_bar)
            errorText = findViewById(R.id.error_text)
            butReload = findViewById(R.id.but_reload)
            errorContainer = findViewById(R.id.error_container)
            toolbar = findViewById(R.id.toolbar)
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_outline_28)
        (requireActivity() as MainActivity).apply {
            setSupportActionBar(toolbar)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = GridLayoutManager(this.context, NUM_OF_COLUMNS)
        viewAdapter = ProductsViewAdapter(emptyList(), this, getImageWidth())
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        (requireActivity().application as App).let {
            marketsRepository = it.marketsRepository
            productsRepository = it.productsRepository
        }

        butReload.setOnClickListener {
            if (marketsRepository.currentCity.isFailed()) {
                marketsRepository.fetchCities()
            } else if (marketsRepository.currentMarkets.isFailed()) {
                marketsRepository.fetchNewData()
            }
        }
        marketId = arguments?.getInt(KEY_MARKET_ID) ?: return
        val market = marketsRepository.currentMarkets.data.markets.find { it.id == marketId } ?: return
        showMarketName(market.title)

        productsRepository.getProducts(marketId).observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> {
                    showLoaded()
                    viewAdapter.products = it.data.products
                }
                is RequestStatus.Fail -> if (it.data.products.isEmpty()) showError(it.message)
                is RequestStatus.Loading -> if (!it.quiet) showLoading()
            }
        }
        toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }
    }

    override fun fetchNewData() = productsRepository.fetchNewData(marketId, true)

    override fun showMoreInfo(product: Product, sharedItem: View) {
        (requireActivity() as MainActivity).navigateToProductInfo(
            marketId = marketId,
            productId = product.id,
            sharedView = sharedItem)
    }

    private fun showMarketName(marketName: String) {
        requireActivity().title = getString(R.string.title_fragment_list_of_products, marketName)
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        errorContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showLoaded() {
        progressBar.visibility = View.GONE
        errorContainer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun getImageWidth() = (requireActivity() as MainActivity).getScreenWidth() / 2

    companion object {
        const val TAG = "ProductsListFragment"
        const val KEY_MARKET_ID = "key_market_id"
        const val NUM_OF_COLUMNS = 2
    }
}
