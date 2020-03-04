package ru.zakoulov.vkcupe.ui.poster

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.zakoulov.vkcupe.App
import ru.zakoulov.vkcupe.R
import ru.zakoulov.vkcupe.data.PostsRepository
import ru.zakoulov.vkcupe.data.source.PostStatus
import ru.zakoulov.vkcupe.utils.getAbsolutePathUri
import ru.zakoulov.vkcupe.utils.getResizedBitmapImage
import ru.zakoulov.vkcupe.utils.hideKeyboard

class PostEditorFragment : BottomSheetDialogFragment() {

    private lateinit var postImage: ImageView
    private lateinit var butCloseBs: View
    private lateinit var butPost: Button
    private lateinit var bsBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var postMessageView: EditText
    private lateinit var postsRepository: PostsRepository

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_post_editor, container, false)
        with (root) {
            postImage = findViewById(R.id.image_post)
            butCloseBs = findViewById(R.id.but_close_bs)
            butPost = findViewById(R.id.but_post)
            postMessageView = findViewById(R.id.post_message_view)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsRepository = (requireActivity().application as App).postsRepository

        val imageUri: Uri = arguments?.getParcelable(KEY_IMAGE_URI) ?: return
        postImage.clipToOutline = true
        postImage.viewTreeObserver.addOnGlobalLayoutListener( object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                postImage.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (context != null) {
                    postImage.setImageBitmap(imageUri.getResizedBitmapImage(postImage.width, requireContext()))
                }
            }
        })
        butCloseBs.setOnClickListener {
            hideBs()
        }
        butPost.setOnClickListener {
            hideKeyboard()
            postsRepository.addPost(postMessageView.editableText.toString(),
                listOf(imageUri.getAbsolutePathUri(requireContext())))
        }
        postsRepository.postStatus.observe(viewLifecycleOwner) {
            when (it) {
                is PostStatus.Loading -> showPostLoading()
                is PostStatus.Success -> {
                    postsRepository.postStatus.value = null
                    showPostLoadingSuccess()
                }
                is PostStatus.Fail -> {
                    postsRepository.postStatus.value = null
                    showPostLoadingFail()
                }
            }
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

    private fun showPostLoading() {
        butPost.isEnabled = false
    }

    private fun showPostLoadingSuccess() {
        butPost.isEnabled = true
        hideBs()
        Toast.makeText(context, R.string.post_publish_success, Toast.LENGTH_LONG).show()
    }

    private fun showPostLoadingFail() {
        butPost.isEnabled = true
        Toast.makeText(context, R.string.post_publish_fail, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val KEY_IMAGE_URI = "key_image_uri"
    }
}
