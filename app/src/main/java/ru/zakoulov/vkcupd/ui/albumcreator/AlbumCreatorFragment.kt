package ru.zakoulov.vkcupd.ui.albumcreator

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.zakoulov.vkcupd.App
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.AlbumsRepository
import ru.zakoulov.vkcupd.utils.hideKeyboard

class AlbumCreatorFragment : BottomSheetDialogFragment() {

    private lateinit var butCloseBs: View
    private lateinit var butCreate: Button
    private lateinit var bsBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var albumNewTitleView: EditText

    private lateinit var albumsRepository: AlbumsRepository

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_album_creator, container, false)
        with (root) {
            butCloseBs = findViewById(R.id.but_close_bs)
            butCreate = findViewById(R.id.but_create)
            albumNewTitleView = findViewById(R.id.album_new_title)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumsRepository = App.getApp(requireActivity().application).albumsRepository

        butCreate.setOnClickListener {
            hideKeyboard()
            dismiss()
            albumsRepository.createAlbum(albumNewTitleView.editableText.toString())
        }
        butCloseBs.setOnClickListener {
            dismiss()
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
}
