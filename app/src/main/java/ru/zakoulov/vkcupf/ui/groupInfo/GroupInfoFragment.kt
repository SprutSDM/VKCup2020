package ru.zakoulov.vkcupf.ui.groupInfo

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import ru.zakoulov.vkcupf.App
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.GroupInfo
import ru.zakoulov.vkcupf.data.GroupRepository
import ru.zakoulov.vkcupf.data.RequestStatus
import ru.zakoulov.vkcupf.utils.DateFormatter
import ru.zakoulov.vkcupf.utils.toShortPretty
import java.util.Locale

class GroupInfoFragment : BottomSheetDialogFragment() {

    private lateinit var groupRepository: GroupRepository

    private lateinit var title: TextView
    private lateinit var followers: TextView
    private lateinit var description: TextView
    private lateinit var bsBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var lastPost: TextView
    private lateinit var butCloseBs: View
    private lateinit var butOpenGroup: Button

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_group_info, container, false)
        with (root) {
            title = findViewById(R.id.group_info_title)
            followers = findViewById(R.id.group_followers)
            description = findViewById(R.id.group_description)
            lastPost = findViewById(R.id.group_last_post)
            butCloseBs = findViewById(R.id.but_close_bs)
            butOpenGroup = findViewById(R.id.but_open_group)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupRepository = (requireActivity().application as App).groupRepository

        val dateFormatter = DateFormatter(
            view.context.getString(R.string.sdf_today),
            view.context.getString(R.string.sdf_yesterday),
            view.context.getString(R.string.sdf_this_year),
            view.context.getString(R.string.sdf_old_year),
            Locale.getDefault())

        val groupId = arguments?.getInt(KEY_GROUP_ID) ?: return dismiss()
        val group = groupRepository.getGroupById(groupId) ?: return dismiss()
        groupRepository.getGroupInfo(group).observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> it.data?.let { setupGroupInfo(it, dateFormatter)}
            }
        }
        title.text = group.title
        description.text = group.description.ifEmpty { "Описание отсутствует" }
        butCloseBs.setOnClickListener {
            hideBs()
        }
        butOpenGroup.setOnClickListener {
            openGroup(groupId)
        }
    }

    private fun setupGroupInfo(groupInfo: GroupInfo, dateFormatter: DateFormatter) {
        val formattedDate = dateFormatter.getFormattedDate(groupInfo.lastPostDate * 1000)
        lastPost.text = getString(R.string.last_post_args, formattedDate)
        val subscribers = resources.getQuantityString(
            R.plurals.subscribers,
            if (groupInfo.membersInGroup < 1000) groupInfo.membersInGroup else 1000,
            groupInfo.membersInGroup.toShortPretty()
        )
        if (groupInfo.friendsInGroup == -1) {
            followers.text = getString(R.string.members_info, subscribers)
        } else {
            val friends = resources.getQuantityString(
                R.plurals.friends,
                if (groupInfo.friendsInGroup < 1000) groupInfo.friendsInGroup else 1000,
                groupInfo.friendsInGroup.toShortPretty()
            )
            followers.text = getString(R.string.followers_info, subscribers, friends)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bsBehavior = bottomSheetDialog.behavior
        bsBehavior.skipCollapsed = true
        bottomSheetDialog.setOnShowListener {
            bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    private fun hideBs() {
        bsBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun openGroup(groupId: Int) {
        val url = "https://vk.com/public$groupId"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), R.string.fail_open_group, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val KEY_GROUP_ID = "key_group_id"
    }
}
