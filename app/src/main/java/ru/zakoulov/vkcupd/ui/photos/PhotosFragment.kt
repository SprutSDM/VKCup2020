package ru.zakoulov.vkcupd.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class PhotosFragment : Fragment(), PhotoCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: PhotosViewAdapter

    private lateinit var albumsRepository: AlbumsRepository
    private var albumId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_photos_list, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            toolbar = findViewById(R.id.toolbar)
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_outline_28)
        MainActivity.getActivity(requireActivity()).apply {
            setSupportActionBar(toolbar)
        }
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
                is RequestStatus.Success -> viewAdapter.photos = it.data.photos
                is RequestStatus.Fail -> showToast(it.message)
                is RequestStatus.Loading -> showToast("LOADING")
            }
        }
        albumsRepository.albums.data.find { it.id == albumId }?.let {
            toolbar.title = it.title
        }
        toolbar.setNavigationOnClickListener {
            MainActivity.getActivity(requireActivity()).navigateBack()
        }
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

    companion object {
        const val TAG = "PhotosFragment"

        const val NUMBER_OF_COLUMNS = 3
        const val KEY_ALBUM_ID = "key_album_id"
    }
}
