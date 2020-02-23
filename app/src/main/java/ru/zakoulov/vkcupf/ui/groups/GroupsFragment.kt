package ru.zakoulov.vkcupf.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupf.App
import ru.zakoulov.vkcupf.MainActivity
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.data.GroupRepository
import ru.zakoulov.vkcupf.data.RequestStatus
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.ui.groupInfo.GroupInfoFragment

class GroupsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var unsubscribeFrame: FrameLayout
    private lateinit var unsubscribeCounter: TextView
    private lateinit var unsubscribe: View
    private lateinit var progressBar: ProgressBar

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
            progressBar = findViewById(R.id.progress_bar)
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
            groupRepository.leaveGroups(viewAdapter.selectedGroups.toList(), object : CommonResponseCallback<Int> {
                override fun success(response: Int) {
                    if (!isAdded) { return }
                    Toast.makeText(requireContext(), R.string.unsubscribe_successful, Toast.LENGTH_LONG).show()
                    viewAdapter
                }

                override fun fail(failMessage: String) {
                    if (!isAdded) { return }
                    Toast.makeText(requireContext(), R.string.unsubscribe_fail, Toast.LENGTH_LONG).show()
                }
            })
        }

        groupRepository.getGroups().observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> {
                    showLoaded()
                    it.data?.let { viewAdapter.groups = it }
                }
                is RequestStatus.Fail -> {
                    showLoaded()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    it.viewed = true
                    (requireActivity() as MainActivity).showError()
                }
                is RequestStatus.Loading -> {
                    showLoading()
                }
            }
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

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun showLoaded() {
        progressBar.visibility = View.GONE
    }

    private fun showGroupInfo(group: Group) {
        val groupInfo = GroupInfoFragment()
        val bundle = Bundle()
        bundle.putInt(GroupInfoFragment.KEY_GROUP_ID, group.id)
        groupInfo.arguments = bundle
        groupInfo.show(requireActivity().supportFragmentManager, groupInfo.tag)
    }

    private val callback = object : GroupsCallback {
        override fun countOfSelectedItemsChanged(count: Int) {
            updateUnsubscribeFrame(count)
        }

        override fun showGroupInfo(group: Group) {
            this@GroupsFragment.showGroupInfo(group)
        }
    }

    companion object {
        val INSTANCE: GroupsFragment by lazy { GroupsFragment() }

        const val NUMBER_OF_COLUMNS = 3
        const val TAG = "GroupsFragment"
    }
}
