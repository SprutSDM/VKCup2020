package ru.zakoulov.vkcupa.data.source

import ru.zakoulov.vkcupa.data.Document

interface DocumentsDataSource {

    fun getDocuments(count: Int, offset: Int, callback: GetDocumentsCallback)

    fun renameDocument(document: Document)

    fun deleteDocument(document: Document)

    interface GetDocumentsCallback {
        fun onSuccess(newDocuments: List<Document>, totalCount: Int)
        fun onFail(message: String)
    }
}
