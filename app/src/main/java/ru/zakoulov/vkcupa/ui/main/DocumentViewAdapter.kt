package ru.zakoulov.vkcupa.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zakoulov.vkcupa.R
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.getDocumentPlaceHolder

class DocumentViewAdapter(documents: List<Document>) : RecyclerView.Adapter<DocumentViewAdapter.DocumentViewHolder>() {

    var documents: List<Document> = documents
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.document_item, parent, false) as View
        return DocumentViewHolder(documentView)
    }

    override fun getItemCount() = documents.size

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]

        holder.documentItem.apply {
            findViewById<ImageView>(R.id.doc_img).setImageDrawable(
                getDocumentPlaceHolder(holder.documentItem.context, document)
            )
            findViewById<TextView>(R.id.doc_title).text = document.title
            findViewById<TextView>(R.id.doc_description).text = document.description
            findViewById<TextView>(R.id.doc_tags).text = document.prettyTags
        }
    }

    class DocumentViewHolder(val documentItem: View): RecyclerView.ViewHolder(documentItem)
}
