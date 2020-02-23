package ru.zakoulov.vkcupf.ui.groupInfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.zakoulov.vkcupf.App
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.GroupRepository

class GroupInfoFragment : BottomSheetDialogFragment() {

    private lateinit var groupRepository: GroupRepository

    private lateinit var title: TextView
    private lateinit var followers: TextView
    private lateinit var description: TextView
    private lateinit var bsBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var lastPost: TextView
    private lateinit var butCloseBs: View

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_group_info, container, false)
        with (root) {
            title = findViewById(R.id.group_info_title)
            followers = findViewById(R.id.group_followers)
            description = findViewById(R.id.group_description)
            lastPost = findViewById(R.id.group_last_post)
            butCloseBs = findViewById(R.id.but_close_bs)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            return
        }
        groupRepository = (requireActivity().application as App).groupRepository

        val groupId = arguments?.getInt(KEY_GROUP_ID) ?: return dismiss()
        val group = groupRepository.getGroupById(groupId) ?: return dismiss()
        groupRepository.getGroupInfo(group)
        title.text = group.title
        butCloseBs.setOnClickListener {
            hideBs()
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

    companion object {
        const val KEY_GROUP_ID = "key_group_id"
    }
}
