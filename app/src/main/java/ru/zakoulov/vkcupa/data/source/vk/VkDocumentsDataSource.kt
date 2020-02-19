package ru.zakoulov.vkcupa.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.source.CommonResponseCallback
import ru.zakoulov.vkcupa.data.source.DocumentsDataSource
import ru.zakoulov.vkcupa.data.source.vk.mappers.DocumentMapper
import ru.zakoulov.vkcupa.data.source.vk.requests.VkDeleteDocumentRequest
import ru.zakoulov.vkcupa.data.source.vk.requests.VkGetDocumentsRequest
import ru.zakoulov.vkcupa.data.source.vk.requests.VkRenameDocumentRequest
import ru.zakoulov.vkcupa.data.source.vk.models.VkDocuments

class VkDocumentsDataSource(
    private val documentMapper: DocumentMapper
) : DocumentsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getDocuments(count: Int, offset: Int, callback: CommonResponseCallback<List<Document>>) {
        VK.execute(VkGetDocumentsRequest(gson, jsonParser, count, offset), object: VKApiCallback<VkDocuments> {
            override fun success(result: VkDocuments) {
                callback.success(result.items.map { documentMapper.map(it) })
            }

            override fun fail(error: Exception) {
                callback.fail(error.localizedMessage ?: error.message ?: "Error loading docs.")
            }
        })
    }

    override fun renameDocument(document: Document, newTitle: String, callback: CommonResponseCallback<Int>) {
        VK.execute(VkRenameDocumentRequest(ownerId = document.ownerId, docId = document.id, title = newTitle),
            object: VKApiCallback<Int> {
                override fun success(result: Int) {
                    callback.success(result)
                }

                override fun fail(error: Exception) {
                    callback.fail(error.localizedMessage ?: error.message ?: "Error renaming doc.")
                }
            })
    }

    override fun deleteDocument(document: Document, callback: CommonResponseCallback<Int>) {
        VK.execute(VkDeleteDocumentRequest(ownerId = document.ownerId, docId = document.id),
            object: VKApiCallback<Int> {
                override fun success(result: Int) {
                    callback.success(result)
                }

                override fun fail(error: Exception) {
                    callback.fail(error.localizedMessage ?: error.message ?: "Error deleting doc.")
                }
            }
        )
    }
}