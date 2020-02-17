package ru.zakoulov.vkcupa.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.source.DocumentsDataSource
import ru.zakoulov.vkcupa.data.source.vk.mappers.DocumentMapper
import ru.zakoulov.vkcupa.data.source.vk.requests.VkDeleteDocumentRequest
import ru.zakoulov.vkcupa.data.source.vk.requests.VkGetDocumentsRequest
import ru.zakoulov.vkcupa.data.source.vk.responses.GetDocumentsResponse

class VkDocumentsDataSource(
    private val documentMapper: DocumentMapper
) : DocumentsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getDocuments(count: Int, offset: Int, callback: DocumentsDataSource.GetDocumentsCallback) {
        VK.execute(VkGetDocumentsRequest(gson, jsonParser, count, offset), object: VKApiCallback<GetDocumentsResponse> {
            override fun success(result: GetDocumentsResponse) {
                callback.onSuccess(result.items.map { documentMapper.map(it) }, result.count)
            }

            override fun fail(error: Exception) {
                callback.onFail(error.localizedMessage ?: error.message ?: "Error loading docs.")
            }
        })
    }

    override fun renameDocument(document: Document) {

    }

    override fun deleteDocument(document: Document, callback: DocumentsDataSource.DeleteDocumentCallback) {
        VK.execute(VkDeleteDocumentRequest(ownerId = document.ownerId, docId = document.id),
            object: VKApiCallback<Int> {
                override fun success(result: Int) {
                    callback.onSuccess(result)
                }

                override fun fail(error: Exception) {
                    callback.onFail(error.localizedMessage ?: error.message ?: "Error deleting doc.")
                }
            }
        )
    }
}