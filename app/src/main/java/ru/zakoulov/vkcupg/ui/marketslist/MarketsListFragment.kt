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
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.App
import ru.zakoulov.vkcupg.MainActivity
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.data.DatabaseRepository
import ru.zakoulov.vkcupg.data.MarketsRepository
import ru.zakoulov.vkcupg.data.core.RequestStatus
import ru.zakoulov.vkcupg.data.core.StatusLiveData
import ru.zakoulov.vkcupg.data.models.Markets
import ru.zakoulov.vkcupg.ui.citypicker.CityPickerFragment

class MarketsListFragment : Fragment(), MarketCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var butReload: Button
    private lateinit var errorContainer: View
    private lateinit var toolbar: Toolbar
    private lateinit var dropDownIcon: ImageView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: MarketsViewAdapter
    private lateinit var marketsObserver: Observer<RequestStatus<Markets>>
    private var currentMarkets: StatusLiveData<Markets>? = null

    private lateinit var databaseRepository: DatabaseRepository
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
        (requireActivity().application as App).let {
            databaseRepository = it.databaseRepository
            marketsRepository = it.marketsRepository
        }
        butReload.setOnClickListener {
            if (databaseRepository.getCities().isSuccessed()) {
                marketsRepository.fetchNewData(databaseRepository.city.value!!)
            }
        }
        toolbar.setOnClickListener {
            val cityPicker = CityPickerFragment()
            cityPicker.show(requireActivity().supportFragmentManager, cityPicker.tag)
        }

        databaseRepository.getCities().observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> Unit // Will be triggered by databaseRepository.city
                is RequestStatus.Fail -> showError(it.message)
                is RequestStatus.Loading, is RequestStatus.Empty -> showLoading()
            }
        }
        databaseRepository.city.observe(viewLifecycleOwner) {
            requireActivity().title = getString(R.string.title_fragment_list_of_products, it.name)
            dropDownIcon.visibility = View.VISIBLE
            changeObservableMarkets()
        }

        marketsObserver = Observer {
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

    private fun changeObservableMarkets() {
        currentMarkets?.removeObserver(marketsObserver)
        currentMarkets = marketsRepository.getMarkets(databaseRepository.city.value!!)
        currentMarkets?.observe(viewLifecycleOwner, marketsObserver)
        recyclerView.scrollToPosition(0)
    }

    override fun fetchNewData() = marketsRepository.fetchNewData(databaseRepository.city.value!!, true)

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
