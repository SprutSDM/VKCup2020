package ru.zakoulov.vkcupa.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupa.data.Document
import ru.zakoulov.vkcupa.data.source.DocumentsDataSource
import ru.zakoulov.vkcupa.data.source.vk.mappers.DocumentMapper
import ru.zakoulov.vkcupa.data.source.vk.requests.VkDocumentsRequest
import ru.zakoulov.vkcupa.data.source.vk.responses.DocumentsResponse

class VkDocumentsDataSource(
    private val documentMapper: DocumentMapper
) : DocumentsDataSource {
    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getDocuments(count: Int, offset: Int, callback: DocumentsDataSource.GetDocumentsCallback) {
        VK.execute(VkDocumentsRequest(gson, jsonParser, count, offset), object: VKApiCallback<DocumentsResponse> {
            override fun success(result: DocumentsResponse) {
                callback.onSuccess(result.items.map { documentMapper.map(it) }, result.count)
            }

            override fun fail(error: Exception) {
                callback.onFail(error.localizedMessage ?: error.message ?: "Error loading docs.")
            }
        })
    }

    override fun renameDocument(document: Document) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteDocument(document: Document) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}