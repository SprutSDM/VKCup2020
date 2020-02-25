package ru.zakoulov.vkcupg.ui.marketslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.R

class MarketsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: MarketsViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_info, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view_markets)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(this.context)
        viewAdapter = MarketsViewAdapter(emptyList())
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        val INSTANCE: MarketsListFragment by lazy { MarketsListFragment() }

        const val TAG = "MarketsListFragment"
    }
}
