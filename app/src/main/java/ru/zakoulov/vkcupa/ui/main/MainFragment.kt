package ru.zakoulov.vkcupa.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupa.App
import ru.zakoulov.vkcupa.R
import ru.zakoulov.vkcupa.data.DocumentRepository
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DocumentViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var documentRepository: DocumentRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        with (root) {
            progressBar = findViewById(R.id.progress_bar)
            recyclerView = findViewById(R.id.recycler_view)
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
        viewAdapter = DocumentViewAdapter(emptyList(), documentRepository, dateFormatter)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        activity?.setTitle(R.string.fragment_documents_title)
        documentRepository.getDocuments().observe(viewLifecycleOwner) {
            viewAdapter.documents = it
        }
        documentRepository.getMessage().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
        }
        documentRepository.isDocsLoadingProgress().observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    companion object {
        val instance: MainFragment by lazy { MainFragment() }
    }
}
