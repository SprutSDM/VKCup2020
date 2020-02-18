package ru.zakoulov.vkcupa.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.zakoulov.vkcupa.data.source.DocumentsDataSource

class DocumentRepository(
    private val remoteSource: DocumentsDataSource
) {
    private var documents: MutableLiveData<List<Document>> = MutableLiveData(emptyList())
    fun getDocuments(): LiveData<List<Document>> = documents

    private var docsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    fun isDocsLoading(): LiveData<Boolean> = docsLoading

    private var message: MutableLiveData<String> = MutableLiveData()
    fun getMessage(): LiveData<String> = message

    fun loadDocuments(count: Int = DOCUMENTS_FOR_LOADING) {
        if (docsLoading.value == true) {
            return
        }
        docsLoading.value = true
        remoteSource.getDocuments(count, documents.value?.size ?: 0,
            object : DocumentsDataSource.GetDocumentsCallback {
                override fun onSuccess(newDocuments: List<Document>, totalCount: Int) {
                    // It's ok because we have init data in LiveData
                    documents.value = documents.value!! + newDocuments

                    docsLoading.value = false
                }

                override fun onFail(failMessage: String) {
                    message.value = failMessage
                    docsLoading.value = false
                }
            }
        )
    }

    fun renameDocument(document: Document) = Unit

    fun deleteDocument(document: Document) {
        remoteSource.deleteDocument(document,
            object: DocumentsDataSource.DeleteDocumentCallback {
                override fun onSuccess(response: Int) {
                    if (response == 1) {
                        documents.value = documents.value!! - document
                    }
                }

                override fun onFail(failMessage: String) {
                    message.value = failMessage
                }
            })
    }

    companion object {
        const val DOCUMENTS_FOR_LOADING = 20
    }
}
