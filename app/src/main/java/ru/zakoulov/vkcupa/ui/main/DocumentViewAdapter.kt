package ru.zakoulov.vkcupa.ui.main

import android.text.format.Formatter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupa.R
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.DocumentRepository
import ru.zakoulov.vkcupa.data.DocumentType
import ru.zakoulov.vkcupa.data.getDocumentPlaceHolder
import java.util.Locale

class DocumentViewAdapter(
    documents: List<Document>,
    private val documentRepository: DocumentRepository,
    private val dateFormatter: DateFormatter,
    private val renameCallback: MainFragment.RenameCallback
) : RecyclerView.Adapter<DocumentViewAdapter.DocumentViewHolder>() {

    var documents: List<Document> = documents
        set(value) {
            val documentDiffCallback = DocumentDiffCallback(field, value)
            val documentDiffResult = DiffUtil.calculateDiff(documentDiffCallback, false)
            field = value
            documentDiffResult.dispatchUpdatesTo(this)
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
            setItemIcon(this, document.type, document.preview)
            setItemTitle(this, document.title)
            setItemDescription(this, document.fileExtension, document.size, document.date * 1000)
            setItemTags(this, document.tags, document.prettyTags)

            findViewById<View>(R.id.doc_button_options).setOnClickListener {
                val wrapper = ContextThemeWrapper(context, R.style.PopupMenuStyle)
                val popup = PopupMenu(wrapper, it, Gravity.END)
                popup.inflate(R.menu.document_options_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.rename_document -> {
                            renameCallback.renameDocument(document)
                            true
                        }
                        R.id.delete_document -> {
                            documentRepository.deleteDocument(document)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }

        if (position + DOCS_BEFORE_LOAD > documents.size) {
            documentRepository.loadDocuments(15)
        }
    }

    private fun setItemIcon(documentItem: View, type: DocumentType, src: String?) {
        val docImg = documentItem.findViewById<ImageView>(R.id.doc_img)
        docImg.clipToOutline = true
        if (src == null) {
            docImg.setImageDrawable(getDocumentPlaceHolder(documentItem.context, type))
        } else {
            Picasso.get()
                .load(src)
                .fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_placeholder_document_image_72)
                .into(docImg)
        }
    }

    private fun setItemTitle(documentItem: View, title: String) {
        documentItem.findViewById<TextView>(R.id.doc_title).text = title
    }

    private fun setItemDescription(documentItem: View, ext: String, size: Long, dateMillis: Long) {
        val description = "${ext.toUpperCase(Locale.getDefault())} · " +
                "${Formatter.formatFileSize(documentItem.context, size).replace(",", ".")} · " +
                dateFormatter.getFormattedDate(dateMillis)
        documentItem.findViewById<TextView>(R.id.doc_description).text = description
    }

    private fun setItemTags(documentItem: View, tags: List<String>, prettyTags: String) {
        if (tags.isNotEmpty()) {
            documentItem.findViewById<LinearLayout>(R.id.doc_tags_container).visibility = View.VISIBLE
            documentItem.findViewById<TextView>(R.id.doc_tags).text = prettyTags
        } else {
            documentItem.findViewById<LinearLayout>(R.id.doc_tags_container).visibility = View.GONE
        }
    }

    class DocumentViewHolder(val documentItem: View): RecyclerView.ViewHolder(documentItem)

    companion object {
        const val DOCS_BEFORE_LOAD = 20
    }
}
