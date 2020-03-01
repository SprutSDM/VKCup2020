package ru.zakoulov.vkcupd.ui.photos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupd.App
import ru.zakoulov.vkcupd.MainActivity
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.AlbumsRepository
import ru.zakoulov.vkcupd.data.core.RequestStatus
import ru.zakoulov.vkcupd.utils.getAbsolutePathUri

class PhotosFragment : Fragment(), PhotoCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var butReload: Button
    private lateinit var errorContainer: View
    private lateinit var menuItemAdd: View

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: PhotosViewAdapter

    private lateinit var albumsRepository: AlbumsRepository
    private var albumId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_photos_list, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            toolbar = findViewById(R.id.toolbar)
            progressBar = findViewById(R.id.progress_bar)
            errorText = findViewById(R.id.error_text)
            butReload = findViewById(R.id.but_reload)
            errorContainer = findViewById(R.id.error_container)
            menuItemAdd = findViewById(R.id.add)
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_outline_28)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = GridLayoutManager(this.context, NUMBER_OF_COLUMNS)
        viewAdapter = PhotosViewAdapter(emptyList(), this)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        albumId = arguments?.getInt(KEY_ALBUM_ID) ?: return

        albumsRepository = App.getApp(requireActivity().application).albumsRepository
        albumsRepository.getPhotos(albumId).observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> {
                    if (it.data.photos.isEmpty()) {
                        showNoPhotos()
                    } else {
                        showLoaded()
                        viewAdapter.photos = it.data.photos
                    }
                }
                is RequestStatus.Fail -> if (it.data.photos.isEmpty()) showError(it.message)
                is RequestStatus.Loading -> if (!it.quiet) showLoading()
            }
        }
        albumsRepository.albums.data.find { it.id == albumId }?.let {
            toolbar.title = it.title
        }
        if (albumId < 0) {
            menuItemAdd.visibility = View.GONE
        }
        toolbar.setNavigationOnClickListener {
            MainActivity.getActivity(requireActivity()).navigateBack()
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add -> {
                    startChoosingPhoto()
                    true
                }
                else -> false
            }
        }

        butReload.setOnClickListener {
            albumsRepository.fetchNewPhotos(albumId)
        }
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        errorContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showLoaded() {
        progressBar.visibility = View.GONE
        errorContainer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showError() {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorText.setText(R.string.error_load_data)
        butReload.visibility = View.VISIBLE
        errorContainer.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        showError()
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showNoPhotos() {
        showError()
        errorText.setText(R.string.error_not_photos)
        butReload.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showToast(@StringRes message: Int) {
        showToast(getString(message))
    }

    override fun fetchNewData() {
        albumsRepository.fetchNewPhotos(albumId, quiet = true)
    }

    private fun startChoosingPhoto() {
        checkPermission()
    }

    private fun checkPermission() {
        if (isPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE) ||
            isPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            choosePhoto()
        }
    }

    private fun isPermissionDenied(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return requireActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun choosePhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = IMAGE_WILDCARD

        val chooserIntent = Intent.createChooser(pickIntent, getString(R.string.chooser_title))

        startActivityForResult(chooserIntent, PICK_IMAGES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGES && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                 albumsRepository.uploadPhoto(albumId, it.getAbsolutePathUri(requireContext()))
                     .observe(viewLifecycleOwner) {
                         showToast(if (it) {
                             R.string.success_upload_photo
                         } else {
                             R.string.fail_upload_photo
                         })
                     }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val TAG = "PhotosFragment"

        const val NUMBER_OF_COLUMNS = 3
        const val KEY_ALBUM_ID = "key_album_id"
        const val PICK_IMAGES = 48533
        const val PERMISSION_CODE = 48534
        const val IMAGE_WILDCARD = "image/*"
    }
}
