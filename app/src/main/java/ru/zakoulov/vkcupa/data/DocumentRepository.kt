package ru.zakoulov.vkcupa.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.zakoulov.vkcupa.data.source.CommonResponseCallback
import ru.zakoulov.vkcupa.data.source.DocumentsDataSource

class DocumentRepository(
    private val remoteSource: DocumentsDataSource
) {
    private var documents: MutableLiveData<List<Document>> = MutableLiveData(emptyList())
    fun getDocuments(): LiveData<List<Document>> = documents

    private var docsLoading: Boolean = false
    private var docsLoadingProgress: MutableLiveData<Boolean> = MutableLiveData(false)
    fun isDocsLoadingProgress(): LiveData<Boolean> = docsLoadingProgress

    private var message: MutableLiveData<String> = MutableLiveData()
    fun getMessage(): LiveData<String> = message

    fun loadDocuments(count: Int = DOCUMENTS_FOR_LOADING, trackProgress: Boolean = false) {
        if (docsLoading) {
            return
        }
        docsLoading = true
        if (trackProgress) {
            docsLoadingProgress.value = true
        }
        remoteSource.getDocuments(count, documents.value?.size ?: 0,
            object : CommonResponseCallback<List<Document>> {
                override fun success(response: List<Document>) {
                    // It's ok because we have init data in LiveData
                    documents.value = documents.value!! + response

                    docsLoading = false
                    docsLoadingProgress.value = false
                }

                override fun fail(failMessage: String) {
                    message.value = failMessage

                    docsLoading = false
                    if (trackProgress) {
                        docsLoadingProgress.value = false
                    }
                }
            })
    }

    fun renameDocument(document: Document, newTitle: String) {
        remoteSource.renameDocument(document, newTitle,
            object : CommonResponseCallback<Int> {
                override fun success(response: Int) {
                    if (response == 1) {
                        documents.value = documents.value?.map {
                            if (it.id == document.id) {
                                document.copy(title = newTitle)
                            } else {
                                it
                            }
                        }
                    }
                }

                override fun fail(failMessage: String) {
                    message.value = failMessage
                }
            })
    }

    fun deleteDocument(document: Document) {
        remoteSource.deleteDocument(document,
            object : CommonResponseCallback<Int> {
                override fun success(response: Int) {
                    if (response == 1) {
                        documents.value = documents.value!! - document
                    }
                }

                override fun fail(failMessage: String) {
                    message.value = failMessage
                }
            })
    }

    companion object {
        const val DOCUMENTS_FOR_LOADING = 20
    }
}
