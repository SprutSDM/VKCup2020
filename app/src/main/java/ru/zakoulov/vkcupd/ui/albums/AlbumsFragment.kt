package ru.zakoulov.vkcupd.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupd.App
import ru.zakoulov.vkcupd.R
import ru.zakoulov.vkcupd.data.AlbumsRepository
import ru.zakoulov.vkcupd.data.core.RequestStatus

class AlbumsFragment : Fragment(), AlbumCallbacks {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: AlbumsViewAdapter

    private lateinit var albumsRepository: AlbumsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_album_list, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = GridLayoutManager(this.context, NUMBER_OF_COLUMNS)
        viewAdapter = AlbumsViewAdapter(emptyList(), this)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        albumsRepository = App.getApp(requireActivity().application).albumsRepository

        albumsRepository.albums.observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> viewAdapter.albums = it.data
                is RequestStatus.Fail -> showToast(it.message)
                is RequestStatus.Loading -> showToast("LOADING")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showToast(@StringRes message: Int) {
        showToast(getString(message))
    }

    override fun fetchNewData() {
        albumsRepository.fetchNewAlbums(quiet = true)
    }

    companion object {
        val instance: AlbumsFragment by lazy { AlbumsFragment() }

        const val NUMBER_OF_COLUMNS = 2
        const val TAG = "AlbumsFragment"
    }
}
