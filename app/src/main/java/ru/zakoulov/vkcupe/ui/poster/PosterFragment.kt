package ru.zakoulov.vkcupe.ui.poster

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.zakoulov.vkcupe.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast

class PosterFragment : Fragment() {

    private lateinit var butChoose: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_poster, container, false)
        with (root) {
            butChoose = findViewById(R.id.but_choose)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        butChoose.setOnClickListener {
            startChoosingPhoto()
        }
    }

    private fun startChoosingPhoto() {
        checkPermission()
    }

    private fun choosePhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = IMAGE_WILDCARD

        val chooserIntent = Intent.createChooser(pickIntent, "Select Picture")
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                choosePhoto()
            }
        } else {
            choosePhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto()
            } else {
                //permission from popup denied
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                showPostEditor(it)
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showPostEditor(selectedImage: Uri) {
        val postEditor = PostEditorFragment()
        val bundle = Bundle()
        bundle.putString(PostEditorFragment.KEY_IMAGE_URI, selectedImage.toString())
        postEditor.arguments = bundle
        postEditor.show(requireActivity().supportFragmentManager, postEditor.tag)
    }

    companion object {
        val instance: PosterFragment by lazy { PosterFragment() }

        const val PICK_IMAGE = 48533
        const val PERMISSION_CODE = 48534
        const val IMAGE_WILDCARD = "image/*"
    }
}
