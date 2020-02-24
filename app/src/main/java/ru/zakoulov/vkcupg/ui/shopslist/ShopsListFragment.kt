package ru.zakoulov.vkcupg.ui.shopslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupg.R
import ru.zakoulov.vkcupg.ui.error.ErrorFragment

class ShopsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: ShopsViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shops, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view_shops)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(this.context)
        viewAdapter = ShopsViewAdapter(emptyList())
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        val INSTANCE: ErrorFragment by lazy { ErrorFragment() }

        const val TAG = "ShopsListFragment"
    }
}
