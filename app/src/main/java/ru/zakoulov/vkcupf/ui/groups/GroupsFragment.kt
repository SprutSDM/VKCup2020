package ru.zakoulov.vkcupf.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.ui.groupInfo.GroupInfoFragment

class GroupsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var unsubscribeFrame: FrameLayout
    private lateinit var unsubscribeCounter: TextView
    private lateinit var unsubscribe: View

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: GroupsViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_groups, container, false)
        with (root) {
            recyclerView = findViewById(R.id.recycler_view)
            unsubscribeFrame = findViewById(R.id.unsubscribe_frame)
            unsubscribeCounter = findViewById(R.id.unsubscribe_counter)
            unsubscribe = findViewById(R.id.unsubscribe)
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
                title = "Пикабу13"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу14"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу15"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу16"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу17"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу18"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу19"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу20"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу21"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу22"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу23"),
            Group(img = "https://sun9-59.userapi.com/c855736/v855736730/24450/q37RDmw7vXA.jpg?ava=1",
                title = "Пикабу24")
        ), callback)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        unsubscribe.setOnClickListener {
            Log.d("abacaba" , viewAdapter.getSelectedGroups().toString())
        }
    }

    private fun updateUnsubscribeFrame(countSelectedGroups: Int) {
        if (countSelectedGroups == 0) {
            unsubscribeFrame.visibility = View.GONE
        } else {
            unsubscribeFrame.visibility = View.VISIBLE
            unsubscribeCounter.text = countSelectedGroups.toString()
        }
    }

    private fun showGroupInfo(group: Group) {
        val groupInfo = GroupInfoFragment()
        val bundle = Bundle()
        bundle.putString(GroupInfoFragment.KEY_GROUP_TITLE, group.title)
        bundle.putString(GroupInfoFragment.KEY_GROUP_FOLLOWERS, "followers")
        bundle.putString(GroupInfoFragment.KEY_GROUP_DESCRIPTION, "description")
        bundle.putString(GroupInfoFragment.KEY_GROUP_LAST_POST, "last post was today")
        groupInfo.arguments = bundle
        groupInfo.show(requireActivity().supportFragmentManager, groupInfo.tag)
    }

    private val callback = object : GroupsCallback {
        override fun countOfSelectedItemsChanged(count: Int) {
            updateUnsubscribeFrame(count)
            Log.d("abacaba", "$count")
        }

        override fun showGroupInfo(group: Group) {
            this@GroupsFragment.showGroupInfo(group)
        }
    }

    companion object {
        val INSTANCE: GroupsFragment by lazy { GroupsFragment() }

        const val NUMBER_OF_COLUMNS = 3
    }
}
