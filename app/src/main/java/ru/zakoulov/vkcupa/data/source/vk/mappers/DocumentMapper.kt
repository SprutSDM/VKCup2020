package ru.zakoulov.vkcupa.data.source.vk.mappers

import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.DocumentType
import ru.zakoulov.vkcupa.data.Mapper
import ru.zakoulov.vkcupa.data.source.vk.models.VkDocument

class DocumentMapper : Mapper<VkDocument, Document> {

    override fun map(input: VkDocument): Document {
        return Document(
            id = input.id,
            title = input.title,
            size = input.size,
            type = when (input.type) {
                1 -> DocumentType.TEXT
                2 -> DocumentType.ZIP
                3, 4 -> DocumentType.IMAGE
                5 -> DocumentType.AUDIO
                6 -> DocumentType.VIDEO
                7 -> DocumentType.BOOK
                else -> DocumentType.TEXT
            },
            fileExtension = input.ext,
            date = input.date,
            tags = input.tags ?: emptyList(),
            preview = input.preview?.photo?.sizes?.find { it.type == "m" }?.src ?: input.preview?.graffiti?.src
        )
    }
}
