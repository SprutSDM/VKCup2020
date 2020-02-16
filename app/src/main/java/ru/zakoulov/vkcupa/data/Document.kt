package ru.zakoulov.vkcupa.data

import android.content.Context
import android.graphics.drawable.Drawable
import ru.zakoulov.vkcupa.R
import java.util.*

data class Document(
    val title: String,
    val size: String,
    val type: DocumentType,
    val fileExtension: String,
    val date: String,
    val tags: List<String>
) {
    val description = "${fileExtension.toUpperCase(Locale.getDefault())} · $size · $date"
    val prettyTags = tags.joinToString(", ")
}

enum class DocumentType {
    AUDIO,
    VIDEO,
    BOOK,
    TEXT,
    ZIP,
    OTHER
}

fun getDocumentPlaceHolder(context: Context, document: Document?): Drawable? {
    document ?: return null
    return when (document.type) {
        DocumentType.AUDIO -> context.getDrawable(R.drawable.placeholder_audio)
        DocumentType.VIDEO -> context.getDrawable(R.drawable.placeholder_video)
        DocumentType.BOOK -> context.getDrawable(R.drawable.placeholder_book)
        DocumentType.TEXT -> context.getDrawable(R.drawable.placeholder_text)
        DocumentType.ZIP -> context.getDrawable(R.drawable.placeholder_zip)
        else -> context.getDrawable(R.drawable.placeholder_zip)
    }
}
