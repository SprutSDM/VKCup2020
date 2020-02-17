package ru.zakoulov.vkcupa.data.source

import ru.zakoulov.vkcupa.data.Document

interface DocumentsDataSource {

    fun getDocuments(count: Int, offset: Int, callback: GetDocumentsCallback)

    fun renameDocument(document: Document)

    fun deleteDocument(document: Document, callback: DeleteDocumentCallback)

    interface GetDocumentsCallback {
        fun onSuccess(newDocuments: List<Document>, totalCount: Int)
        fun onFail(message: String)
    }

    interface DeleteDocumentCallback {
        fun onSuccess(response: Int)
        fun onFail(message: String)
    }
}
