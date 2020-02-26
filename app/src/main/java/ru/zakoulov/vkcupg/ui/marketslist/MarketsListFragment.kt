package ru.zakoulov.vkcupg.ui.marketslist

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.App
import ru.zakoulov.vkcupg.MainActivity
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.MarketsRepository
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.models.Market
import ru.zakoulov.vkcupg.ui.citypicker.CityPickerFragment

class MarketsListFragment : Fragment(), MarketAdapterCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var butReload: Button
    private lateinit var errorContainer: View
    private lateinit var toolbar: Toolbar
    private lateinit var dropDownIcon: ImageView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: MarketsViewAdapter

    private lateinit var marketsRepository: MarketsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_markets, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view_markets)
            progressBar = findViewById(R.id.progress_bar)
            errorText = findViewById(R.id.error_text)
            butReload = findViewById(R.id.but_reload)
            errorContainer = findViewById(R.id.error_container)
            toolbar = findViewById(R.id.toolbar)
            dropDownIcon = findViewById(R.id.dropdown_icon)
        }
        (requireActivity() as MainActivity).apply {
            setSupportActionBar(toolbar)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle(R.string.title_fragment_empty)

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = MarketsViewAdapter(emptyList(), this)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        marketsRepository = (requireActivity().application as App).marketsRepository

        butReload.setOnClickListener {
            if (marketsRepository.currentCity.isFailed()) {
                marketsRepository.fetchCities()
            } else if (marketsRepository.currentMarkets.isFailed()) {
                marketsRepository.fetchNewData()
            }
        }
        toolbar.setOnClickListener {
            val cityPicker = CityPickerFragment()
            cityPicker.show(requireActivity().supportFragmentManager, cityPicker.tag)
        }

        marketsRepository.currentCity.observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> showCityName(it.data.name)
                is RequestStatus.Fail -> showError(it.message)
                is RequestStatus.Loading, is RequestStatus.Empty -> showLoading()
            }
        }

        marketsRepository.currentMarkets.observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> {
                    showLoaded()
                    viewAdapter.markets = it.data.markets
                }
                is RequestStatus.Fail -> if (it.data.markets.isEmpty()) showError(it.message)
                is RequestStatus.Loading -> if (!it.quiet) showLoading()
            }
        }
    }

    override fun fetchNewData() = marketsRepository.fetchNewData(true)

    override fun navigateToProducts(market: Market) = (requireActivity() as MainActivity).navigateToProducts(market.id)

    private fun showCityName(cityName: String) {
        requireActivity().title = getString(R.string.title_fragment_list_of_markets, cityName)
        dropDownIcon.visibility = View.VISIBLE
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

    companion object {
        val INSTANCE: MarketsListFragment by lazy { MarketsListFragment() }

        const val TAG = "MarketsListFragment"
    }
}
