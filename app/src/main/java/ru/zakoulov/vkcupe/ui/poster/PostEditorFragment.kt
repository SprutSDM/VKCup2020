package ru.zakoulov.vkcupe.ui.poster

import android.app.Dialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.zakoulov.vkcupe.R

class PostEditorFragment : BottomSheetDialogFragment() {

    private lateinit var postImage: ImageView

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_post_editor, container, false)
        with (root) {
            postImage = findViewById(R.id.image_post)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUri = arguments?.getString(KEY_IMAGE_URI)!!
        postImage.clipToOutline = true
        postImage.setImageURI(Uri.parse(imageUri))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.behavior.skipCollapsed = true
        bottomSheetDialog.setOnShowListener {
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    companion object {
        const val KEY_IMAGE_URI = "key_image_uri"
    }
}
