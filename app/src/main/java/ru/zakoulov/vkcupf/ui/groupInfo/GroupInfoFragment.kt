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
import ru.zakoulov.vkcupf.R

class GroupInfoFragment : BottomSheetDialogFragment() {

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

        title.text = arguments?.getString(KEY_GROUP_TITLE) ?: return dismiss()
        followers.text = arguments?.getString(KEY_GROUP_FOLLOWERS) ?: return dismiss()
        description.text = arguments?.getString(KEY_GROUP_DESCRIPTION) ?: return dismiss()
        lastPost.text = arguments?.getString(KEY_GROUP_LAST_POST) ?: return dismiss()
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
        const val KEY_GROUP_TITLE = "key_group_title"
        const val KEY_GROUP_FOLLOWERS = "key_group_followers"
        const val KEY_GROUP_DESCRIPTION = "key_group_description"
        const val KEY_GROUP_LAST_POST = "key_group_last_post"
    }
}
