package ru.zakoulov.vkcupa.data

import android.content.Context
import android.graphics.drawable.Drawable
import ru.zakoulov.vkcupa.R

data class Document(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Long,
    val type: DocumentType,
    val fileExtension: String,
    val date: Long,
    val tags: List<String>,
    val preview: String?
) {
    val prettyTags = tags.joinToString(", ")
}

enum class DocumentType {
    AUDIO,
    VIDEO,
    BOOK,
    TEXT,
    ZIP,
    IMAGE,
    OTHER
}

fun getDocumentPlaceHolder(context: Context, type: DocumentType?): Drawable? {
    type ?: return null
    return when (type) {
        DocumentType.AUDIO -> context.getDrawable(R.drawable.placeholder_audio)
        DocumentType.VIDEO -> context.getDrawable(R.drawable.placeholder_video)
        DocumentType.BOOK -> context.getDrawable(R.drawable.placeholder_book)
        DocumentType.TEXT -> context.getDrawable(R.drawable.placeholder_text)
        DocumentType.ZIP -> context.getDrawable(R.drawable.placeholder_zip)
        else -> context.getDrawable(R.drawable.placeholder_text)
    }
}
