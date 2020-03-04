package ru.zakoulov.vkcupa.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupa.App
import ru.zakoulov.vkcupa.MainActivity
import ru.zakoulov.vkcupa.R
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.DocumentRepository
import ru.zakoulov.vkcupa.ui.renameDialog.RenameDialogFragment
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DocumentViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var documentRepository: DocumentRepository
    private lateinit var errorContainer: View
    private lateinit var errorText: TextView
    private lateinit var butReload: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        with (root) {
            progressBar = findViewById(R.id.progress_bar)
            recyclerView = findViewById(R.id.recycler_view)
            errorContainer = findViewById(R.id.error_container)
            errorText = findViewById(R.id.error_text)
            butReload = findViewById(R.id.but_reload)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        documentRepository = (requireActivity().application as App).documentRepository
        viewManager = LinearLayoutManager(this.context)
        val dateFormatter = DateFormatter(
            view.context.getString(R.string.sdf_today),
            view.context.getString(R.string.sdf_yesterday),
            view.context.getString(R.string.sdf_this_year),
            view.context.getString(R.string.sdf_old_year),
            Locale.getDefault())
        viewAdapter = DocumentViewAdapter(emptyList(), documentRepository, dateFormatter, renameCallback)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        activity?.setTitle(R.string.fragment_documents_title)
        documentRepository.getDocuments().observe(viewLifecycleOwner) {
            viewAdapter.documents = it
        }
        documentRepository.isDocsLoadingProgress().observe(viewLifecycleOwner) {
            if (it) {
                showLoading()
            } else {
                if (documentRepository.getDocuments().value?.size == 0) {
                    if ((requireActivity() as MainActivity).isInternetAvailable()) {
                        showNoDocsFound()
                    } else {
                        showNetworkError()
                    }
                } else {
                    showLoaded()
                }
            }
        }
        butReload.setOnClickListener {
            documentRepository.loadDocuments(trackProgress = true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_RENAME_DOC && resultCode == Activity.RESULT_OK) {
            val docId = data?.getIntExtra(KEY_DOC_ID, 0) ?: 0
            val newDocTitle = data?.getStringExtra(KEY_DOC_TITLE) ?: ""
            documentRepository.getDocuments().value?.find { it.id == docId }?.let {
                documentRepository.renameDocument(it, newDocTitle)
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
    }

    private fun showLoaded() {
        progressBar.visibility = View.GONE
        errorContainer.visibility = View.GONE
    }

    private fun showError(@StringRes error: Int) {
        progressBar.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        errorText.text = getString(error)
    }

    private fun showNoDocsFound() = showError(R.string.no_docs_found)

    private fun showNetworkError() = showError(R.string.error_doc_load)

    interface AdapterCallbacks {
        fun renameDocument(doc: Document)

        fun openDocument(doc: Document)
    }

    private val renameCallback = object : AdapterCallbacks {
        override fun renameDocument(doc: Document) {
            val dialogFragment = RenameDialogFragment()
            val bundle = Bundle()
            bundle.putString(KEY_DOC_TITLE, doc.title)
            bundle.putInt(KEY_DOC_ID, doc.id)
            dialogFragment.arguments = bundle
            dialogFragment.setTargetFragment(this@MainFragment, REQUEST_RENAME_DOC)
            dialogFragment.show(requireActivity().supportFragmentManager, dialogFragment::class.java.name)
        }

        override fun openDocument(doc: Document) = (requireActivity() as MainActivity).openDocsViewer(doc)
    }

    companion object {
        val instance: MainFragment by lazy { MainFragment() }

        const val KEY_DOC_TITLE = "key_doc_title"
        const val KEY_DOC_ID = "key_doc_id"
        const val REQUEST_RENAME_DOC = 6565656
    }
}
