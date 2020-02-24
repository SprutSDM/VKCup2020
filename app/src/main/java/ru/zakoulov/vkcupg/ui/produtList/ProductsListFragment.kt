package ru.zakoulov.vkcupg.ui.produtList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.ui.error.ErrorFragment

class ProductsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: ProductsViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view_shops)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = GridLayoutManager(this.context, NUM_OF_COLUMNS)
        viewAdapter = ProductsViewAdapter(emptyList())
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        val INSTANCE: ErrorFragment by lazy { ErrorFragment() }

        const val TAG = "ProductsListFragment"
        const val NUM_OF_COLUMNS = 2
    }
}
