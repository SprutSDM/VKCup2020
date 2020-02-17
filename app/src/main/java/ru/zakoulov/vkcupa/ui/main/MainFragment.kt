package ru.zakoulov.vkcupa.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupa.App
import ru.zakoulov.vkcupa.R
import ru.zakoulov.vkcupa.data.DocumentRepository
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DocumentViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var documentRepository: DocumentRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
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
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        activity?.setTitle(R.string.fragment_documents_title)
        documentRepository.getDocuments().observe(viewLifecycleOwner) {
            viewAdapter.documents = it
        }
    }

    companion object {
        val instance: MainFragment by lazy { MainFragment() }
    }
}
