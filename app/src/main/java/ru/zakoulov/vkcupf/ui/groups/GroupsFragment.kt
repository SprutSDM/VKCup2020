package ru.zakoulov.vkcupf.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.Group

class GroupsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: GroupsViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_groups, container, false)
        with (root) {
            recyclerView = findViewById(R.id.recycler_view)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = GridLayoutManager(this.context, NUMBER_OF_COLUMNS)
        viewAdapter = GroupsViewAdapter(listOf(
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу1"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу2"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу3"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу4"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу5"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу6"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу7"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу8"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу9"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу10"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу11"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу12"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу13")
        ), callback)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private val callback = object : GroupsCallback {
        override fun countOfSelectedItemsChanged(count: Int) {
            Log.d("abacaba", "$count")
        }
    }

    companion object {
        val INSTANCE: GroupsFragment by lazy { GroupsFragment() }

        const val NUMBER_OF_COLUMNS = 3
    }
}
