package ru.zakoulov.vkcupa.data.source

import ru.zakoulov.vkcupa.data.Document

interface DocumentsDataSource {

    fun getDocuments(count: Int, offset: Int, callback: CommonResponseCallback<List<Document>>)

    fun renameDocument(document: Document, newTitle: String, callback: CommonResponseCallback<Int>)

    fun deleteDocument(document: Document, callback: CommonResponseCallback<Int>)
}
