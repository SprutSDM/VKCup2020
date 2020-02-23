package ru.zakoulov.vkcupf.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupf.App
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.data.GroupRepository
import ru.zakoulov.vkcupf.ui.groupInfo.GroupInfoFragment

class GroupsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var unsubscribeFrame: FrameLayout
    private lateinit var unsubscribeCounter: TextView
    private lateinit var unsubscribe: View

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: GroupsViewAdapter
    private lateinit var groupRepository: GroupRepository

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
        groupRepository = (requireActivity().application as App).groupRepository
        viewManager = GridLayoutManager(this.context, NUMBER_OF_COLUMNS)
        viewAdapter = GroupsViewAdapter(emptyList(), callback)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        unsubscribe.setOnClickListener {
            Log.d("abacaba" , viewAdapter.getSelectedGroups().toString())
        }

        groupRepository.getGroups().observe(viewLifecycleOwner) {
            viewAdapter.setGroups(it)
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
