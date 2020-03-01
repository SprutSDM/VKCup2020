package ru.zakoulov.vkcupd.ui.albums

import android.os.Bundle
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
import ru.zakoulov.vkcupd.data.models.Album
import ru.zakoulov.vkcupd.ui.albumcreator.AlbumCreatorFragment
import ru.zakoulov.vkcupd.ui.albums.viewStrategy.AlbumOpenPhotosStrategy
import ru.zakoulov.vkcupd.ui.albums.viewStrategy.AlbumRemoveAlbumsStrategy
import ru.zakoulov.vkcupd.ui.albums.viewStrategy.AlbumViewStrategy
import ru.zakoulov.vkcupd.ui.albums.viewStrategy.AlbumViewStrategyImpl
import ru.zakoulov.vkcupd.ui.albums.viewStrategy.AlbumsStrategyCallbacks

class AlbumsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var butReload: Button
    private lateinit var errorContainer: View
    private lateinit var toolbar: Toolbar
    private lateinit var menuItemAdd: View
    private lateinit var menuItemEdit: View

    private lateinit var viewManager: GridLayoutManager
    private lateinit var viewAdapter: AlbumsViewAdapter

    private lateinit var albumsRepository: AlbumsRepository

    private val albumsStrategy: AlbumViewStrategy = AlbumViewStrategyImpl(
        AlbumOpenPhotosStrategy(),
        AlbumRemoveAlbumsStrategy(),
        StrategyCallbacks()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_album_list, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            progressBar = findViewById(R.id.progress_bar)
            errorText = findViewById(R.id.error_text)
            butReload = findViewById(R.id.but_reload)
            errorContainer = findViewById(R.id.error_container)
            toolbar = findViewById(R.id.toolbar)
            menuItemAdd = findViewById(R.id.add)
            menuItemEdit = findViewById(R.id.edit)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewManager = GridLayoutManager(this.context, NUMBER_OF_COLUMNS)
        viewAdapter = AlbumsViewAdapter(emptyList(), AdapterCallbacks())
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        albumsRepository = App.getApp(requireActivity().application).albumsRepository

        albumsRepository.albums.observe(viewLifecycleOwner) {
            when (it) {
                is RequestStatus.Success -> {
                    showLoaded()
                    viewAdapter.albums = it.data
                }
                is RequestStatus.Fail -> showError(it.message)
                is RequestStatus.Loading -> if (!it.quiet) showLoading()
            }
        }

        butReload.setOnClickListener {
            albumsRepository.fetchNewAlbums()
        }

        toolbar.setOnMenuItemClickListener {
            albumsStrategy.onMenuItemClicked(it)
        }

        toolbar.setNavigationOnClickListener {
            albumsStrategy.onNavButtonClicked()
        }

        albumsStrategy.showInterface()
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

    private fun showError(message: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showToast(@StringRes message: Int) {
        showToast(getString(message))
    }

    inner class StrategyCallbacks : AlbumsStrategyCallbacks {
        override fun openPhotos(album: Album) {
            MainActivity.getActivity(requireActivity()).navigateToAlbumPhotos(album.id)
        }

        override fun removeAlbum(album: Album) {
            if (album.id < 0) {
                showToast(R.string.unable_to_remove_album)
                return
            }
            albumsRepository.removeAlbum(album.id).observe(viewLifecycleOwner) {
                showToast(if (it) R.string.success_remove_album else R.string.fail_remove_album)
            }
        }

        override fun createAlbum() {
            val fragment = AlbumCreatorFragment()
            fragment.show(requireActivity().supportFragmentManager, fragment.tag)
        }

        override fun editAlbum() {
            // TODO
        }

        override fun showRemoveAlbumsInterface() {
            toolbar.setNavigationIcon(R.drawable.ic_cancel_outlne_28)
            menuItemAdd.visibility = View.GONE
            menuItemEdit.visibility = View.GONE
            toolbar.setTitle(R.string.albums_edit_title)
            viewAdapter.transformAlbumsToRemoving()
        }

        override fun showOpenPhotosInterface() {
            toolbar.navigationIcon = null
            menuItemAdd.visibility = View.VISIBLE
            menuItemEdit.visibility = View.VISIBLE
            toolbar.setTitle(R.string.albums_title)
            viewAdapter.transformAlbumsToOpening()
        }
    }

    inner class AdapterCallbacks : AlbumAdapterCallbacks {
        override fun fetchNewData() {
            albumsRepository.fetchNewAlbums(quiet = true)
        }

        override fun onAlbumClicked(album: Album) {
            albumsStrategy.onAlbumClicked(album)
        }

        override fun onAlbumLongClicker(album: Album) {
            albumsStrategy.onAlbumLongClicked(album)
        }
    }

    companion object {
        val instance: AlbumsFragment by lazy { AlbumsFragment() }

        const val NUMBER_OF_COLUMNS = 2
        const val TAG = "AlbumsFragment"
    }
}
