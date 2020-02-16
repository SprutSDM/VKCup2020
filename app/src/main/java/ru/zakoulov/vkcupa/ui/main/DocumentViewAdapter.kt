package ru.zakoulov.vkcupa.ui.main

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.widget.PopupWindowCompat
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
            findViewById<View>(R.id.doc_button_options).setOnClickListener {
                val wrapper = ContextThemeWrapper(context, R.style.PopupMenuStyle)
                val popup = PopupMenu(wrapper, it, Gravity.END)
                popup.inflate(R.menu.document_options_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.rename_document -> true
                        R.id.delete_document -> true
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    class DocumentViewHolder(val documentItem: View): RecyclerView.ViewHolder(documentItem)
}
