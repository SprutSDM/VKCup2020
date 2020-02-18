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
    val preview: String?,
    val url: String
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
        DocumentType.AUDIO -> context.getDrawable(R.mipmap.ic_placeholder_document_music_72)
        DocumentType.VIDEO -> context.getDrawable(R.mipmap.ic_placeholder_document_video_72)
        DocumentType.BOOK -> context.getDrawable(R.mipmap.ic_placeholder_document_book_72)
        DocumentType.TEXT -> context.getDrawable(R.mipmap.ic_placeholder_document_text_72)
        DocumentType.ZIP -> context.getDrawable(R.mipmap.ic_placeholder_document_archive_72)
        DocumentType.IMAGE -> context.getDrawable(R.mipmap.ic_placeholder_document_image_72)
        else -> context.getDrawable(R.mipmap.ic_placeholder_document_other_72)
    }
}
